package com.rbleggi.loggerrouter

import java.io.File
import java.time.LocalDateTime

enum class LogLevel(val value: Int) {
    DEBUG(0), INFO(1), WARN(2), ERROR(3), FATAL(4);
    
    fun isEnabled(threshold: LogLevel): Boolean = this.value >= threshold.value
}

data class LogEntry(
    val level: LogLevel,
    val message: String,
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val logger: String = "default",
    val throwable: Throwable? = null,
    val metadata: Map<String, Any> = emptyMap()
) {
    override fun toString(): String {
        val throwableInfo = throwable?.let { " | Exception: ${it.message}" } ?: ""
        val metadataInfo = if (metadata.isNotEmpty()) " | Metadata: $metadata" else ""
        return "[${timestamp}] [${level.name}] [$logger] $message$throwableInfo$metadataInfo"
    }
}

interface LoggingStrategy {
    fun log(entry: LogEntry)
    fun isAvailable(): Boolean
    fun getName(): String
}

class ConsoleLoggingStrategy(private val useColors: Boolean = true) : LoggingStrategy {
    private val colorMap = mapOf(
        "DEBUG" to "\u001B[36m", "INFO" to "\u001B[32m", "WARN" to "\u001B[33m",
        "ERROR" to "\u001B[31m", "FATAL" to "\u001B[35m"
    )
    private val resetColor = "\u001B[0m"
    
    override fun getName(): String = "Console"
    override fun isAvailable(): Boolean = true
    
    override fun log(entry: LogEntry) {
        val formatted = if (useColors) {
            val color = colorMap[entry.level.name] ?: ""
            "$color${entry}$resetColor"
        } else {
            entry.toString()
        }
        println(formatted)
    }
}

class FileSystemLoggingStrategy(
    private val logDirectory: String = "logs",
    private val fileName: String = "application.log"
) : LoggingStrategy {
    
    private val logFile: File by lazy {
        val dir = File(logDirectory)
        if (!dir.exists()) dir.mkdirs()
        File(dir, fileName)
    }
    
    override fun getName(): String = "FileSystem"
    override fun isAvailable(): Boolean = logFile.parentFile?.canWrite() == true
    
    override fun log(entry: LogEntry) {
        try {
            logFile.appendText("${entry}\n")
        } catch (e: Exception) {
            println("File logging failed: ${e.message}")
            println(entry.toString())
        }
    }
}

class ElkLoggingStrategy(
    private val elasticsearchUrl: String = "http://localhost:9200",
    private val indexName: String = "application-logs"
) : LoggingStrategy {
    
    override fun getName(): String = "ELK"
    override fun isAvailable(): Boolean = true
    
    override fun log(entry: LogEntry) {
        try {
            val jsonPayload = createElasticsearchDocument(entry)
            sendToElasticsearch(jsonPayload)
        } catch (e: Exception) {
            println("ELK logging failed: ${e.message}")
            println(entry.toString())
        }
    }
    
    private fun createElasticsearchDocument(entry: LogEntry): String {
        val timestamp = entry.timestamp.toString()
        val throwableInfo = entry.throwable?.let { 
            """, "exception": "${it.javaClass.simpleName}", "message": "${it.message}""""
        } ?: ""
        
        return """{
            "@timestamp": "$timestamp",
            "level": "${entry.level.name}",
            "logger": "${entry.logger}",
            "message": "${entry.message}",
            "metadata": ${entry.metadata.toString()}$throwableInfo
        }"""
    }
    
    private fun sendToElasticsearch(jsonPayload: String) {
        val url = java.net.URL("$elasticsearchUrl/$indexName/_doc")
        val connection = url.openConnection() as java.net.HttpURLConnection
        
        connection.apply {
            requestMethod = "POST"
            setRequestProperty("Content-Type", "application/json")
            connectTimeout = 5000
            readTimeout = 10000
            doOutput = true
        }
        
        connection.outputStream.use { outputStream ->
            outputStream.write(jsonPayload.toByteArray())
        }
        
        val responseCode = connection.responseCode
        if (responseCode !in 200..299) {
            throw Exception("ELK request failed with status: $responseCode")
        }
    }
}

class MemoryLoggingStrategy(private val maxEntries: Int = 1000) : LoggingStrategy {
    private val logs = mutableListOf<LogEntry>()
    
    override fun getName(): String = "Memory"
    override fun isAvailable(): Boolean = true
    
    override fun log(entry: LogEntry) {
        synchronized(logs) {
            logs.add(entry)
            if (logs.size > maxEntries) {
                logs.removeAt(0)
            }
        }
    }
    
    fun getLogs(): List<LogEntry> = synchronized(logs) { logs.toList() }
    fun getLogsByLevel(level: LogLevel): List<LogEntry> = 
        synchronized(logs) { logs.filter { it.level == level } }
    fun getLogsByLogger(logger: String): List<LogEntry> = 
        synchronized(logs) { logs.filter { it.logger == logger } }
    fun clearLogs() = synchronized(logs) { logs.clear() }
    fun getLogCount(): Int = synchronized(logs) { logs.size }
}

class LoggerRouter(
    private val defaultLevel: LogLevel = LogLevel.INFO,
    private val fallbackStrategy: LoggingStrategy = ConsoleLoggingStrategy()
) {
    
    private val strategies = mutableMapOf<String, LoggingStrategy>()
    
    init {
        addStrategy("console", ConsoleLoggingStrategy())
        addStrategy("file", FileSystemLoggingStrategy())
        addStrategy("memory", MemoryLoggingStrategy())
    }
    
    fun addStrategy(name: String, strategy: LoggingStrategy) {
        strategies[name] = strategy
    }
    
    fun removeStrategy(name: String) {
        strategies.remove(name)
    }
    
    fun getAvailableStrategies(): List<String> = strategies.keys.toList()
    fun getWorkingStrategies(): List<String> = strategies.filter { it.value.isAvailable() }.keys.toList()
    
    fun log(
        level: LogLevel,
        message: String,
        logger: String = "default",
        strategy: String? = null,
        throwable: Throwable? = null,
        metadata: Map<String, Any> = emptyMap()
    ) {
        val entry = LogEntry(level, message, logger = logger, throwable = throwable, metadata = metadata)
        
        if (strategy != null) {
            logToStrategy(strategy, entry)
        } else {
            logToAllStrategies(entry)
        }
    }
    
    private fun logToStrategy(strategyName: String, entry: LogEntry) {
        val strategy = strategies[strategyName]
        if (strategy != null && strategy.isAvailable()) {
            try {
                strategy.log(entry)
            } catch (e: Exception) {
                fallbackStrategy.log(LogEntry(
                    LogLevel.ERROR,
                    "Failed to log to strategy '$strategyName': ${e.message}",
                    logger = "LoggerRouter"
                ))
                fallbackStrategy.log(entry)
            }
        } else {
            fallbackStrategy.log(LogEntry(
                LogLevel.WARN,
                "Strategy '$strategyName' not available, using fallback",
                logger = "LoggerRouter"
            ))
            fallbackStrategy.log(entry)
        }
    }
    
    private fun logToAllStrategies(entry: LogEntry) {
        val availableStrategies = strategies.filter { it.value.isAvailable() }
        
        if (availableStrategies.isEmpty()) {
            fallbackStrategy.log(LogEntry(
                LogLevel.WARN,
                "No strategies available, using fallback",
                logger = "LoggerRouter"
            ))
            fallbackStrategy.log(entry)
        } else {
            availableStrategies.forEach { (name, strategy) ->
                try {
                    strategy.log(entry)
                } catch (e: Exception) {
                    fallbackStrategy.log(LogEntry(
                        LogLevel.ERROR,
                        "Failed to log to strategy '$name': ${e.message}",
                        logger = "LoggerRouter"
                    ))
                }
            }
        }
    }
    
    fun debug(message: String, logger: String = "default", strategy: String? = null, metadata: Map<String, Any> = emptyMap()) {
        if (LogLevel.DEBUG.isEnabled(defaultLevel)) {
            log(LogLevel.DEBUG, message, logger, strategy, metadata = metadata)
        }
    }
    
    fun info(message: String, logger: String = "default", strategy: String? = null, metadata: Map<String, Any> = emptyMap()) {
        if (LogLevel.INFO.isEnabled(defaultLevel)) {
            log(LogLevel.INFO, message, logger, strategy, metadata = metadata)
        }
    }
    
    fun warn(message: String, logger: String = "default", strategy: String? = null, throwable: Throwable? = null, metadata: Map<String, Any> = emptyMap()) {
        if (LogLevel.WARN.isEnabled(defaultLevel)) {
            log(LogLevel.WARN, message, logger, strategy, throwable, metadata)
        }
    }
    
    fun error(message: String, logger: String = "default", strategy: String? = null, throwable: Throwable? = null, metadata: Map<String, Any> = emptyMap()) {
        if (LogLevel.ERROR.isEnabled(defaultLevel)) {
            log(LogLevel.ERROR, message, logger, strategy, throwable, metadata)
        }
    }
    
    fun fatal(message: String, logger: String = "default", strategy: String? = null, throwable: Throwable? = null, metadata: Map<String, Any> = emptyMap()) {
        if (LogLevel.FATAL.isEnabled(defaultLevel)) {
            log(LogLevel.FATAL, message, logger, strategy, throwable, metadata)
        }
    }
    
    fun getStrategy(name: String): LoggingStrategy? = strategies[name]
    fun shutdown() = strategies.clear()
}

fun main() {
    val logger = LoggerRouter(defaultLevel = LogLevel.INFO)
    logger.addStrategy("elk", ElkLoggingStrategy())
    
    println("Logger Router Demo")
    println("==================")
    
    println("\nAvailable strategies:")
    logger.getAvailableStrategies().forEach { strategy ->
        val status = if (logger.getWorkingStrategies().contains(strategy)) "OK" else "FAIL"
        println("  $status $strategy")
    }
    
    println("\nLogging examples:")
    
    logger.info("Application started successfully")
    logger.debug("This debug message won't be shown (level is INFO)")
    logger.warn("This is a warning message")
    logger.error("This is an error message")
    
    logger.info("This message goes only to console", strategy = "console")
    logger.info("This message goes only to file", strategy = "file")
    logger.info("This message goes only to memory", strategy = "memory")
    
    logger.info(
        "User login successful",
        logger = "AuthService",
        metadata = mapOf(
            "userId" to "12345",
            "ip" to "192.168.1.100",
            "userAgent" to "Mozilla/5.0..."
        )
    )
    
    try {
        throw RuntimeException("Something went wrong!")
    } catch (e: Exception) {
        logger.error("An exception occurred", throwable = e)
    }
    
    val memoryStrategy = logger.getStrategy("memory") as? MemoryLoggingStrategy
    if (memoryStrategy != null) {
        println("\nMemory strategy logs:")
        memoryStrategy.getLogs().forEach { entry ->
            println("  $entry")
        }
        println("  Total logs in memory: ${memoryStrategy.getLogCount()}")
    }
    
    println("\nDemo completed!")
    logger.shutdown()
}