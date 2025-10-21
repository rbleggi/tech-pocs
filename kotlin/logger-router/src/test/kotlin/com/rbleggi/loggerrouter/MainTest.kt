package com.rbleggi.loggerrouter

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainTest {
    
    private lateinit var logger: LoggerRouter
    private lateinit var memoryStrategy: MemoryLoggingStrategy
    
    @BeforeEach
    fun setUp() {
        memoryStrategy = MemoryLoggingStrategy()
        logger = LoggerRouter(defaultLevel = LogLevel.INFO)
        logger.addStrategy("memory", memoryStrategy)
    }
    
    @Test
    fun `should create logger router with default strategies`() {
        val defaultLogger = LoggerRouter()
        
        assertTrue(defaultLogger.getAvailableStrategies().contains("console"))
        assertTrue(defaultLogger.getAvailableStrategies().contains("file"))
        assertTrue(defaultLogger.getAvailableStrategies().contains("memory"))
    }
    
    @Test
    fun `should add and remove strategies`() {
        val customStrategy = ConsoleLoggingStrategy()
        
        logger.addStrategy("custom", customStrategy)
        assertTrue(logger.getAvailableStrategies().contains("custom"))
        
        logger.removeStrategy("custom")
        assertFalse(logger.getAvailableStrategies().contains("custom"))
    }
    
    @Test
    fun `should log to all available strategies`() {
        logger.info("Test message", logger = "TestLogger")
        
        val memoryLogs = memoryStrategy.getLogs()
        assertEquals(1, memoryLogs.size)
        assertEquals("Test message", memoryLogs[0].message)
        assertEquals(LogLevel.INFO, memoryLogs[0].level)
        assertEquals("TestLogger", memoryLogs[0].logger)
    }
    
    @Test
    fun `should log to specific strategy`() {
        logger.info("Test message", strategy = "memory")
        
        val memoryLogs = memoryStrategy.getLogs()
        assertEquals(1, memoryLogs.size)
        assertEquals("Test message", memoryLogs[0].message)
    }
    
    @Test
    fun `should respect log level filtering`() {
        val debugLogger = LoggerRouter(defaultLevel = LogLevel.DEBUG)
        debugLogger.addStrategy("memory", memoryStrategy)
        
        debugLogger.debug("Debug message")
        debugLogger.info("Info message")
        debugLogger.warn("Warning message")
        
        val memoryLogs = memoryStrategy.getLogs()
        assertEquals(3, memoryLogs.size)
        
        val infoLogger = LoggerRouter(defaultLevel = LogLevel.INFO)
        val infoMemoryStrategy = MemoryLoggingStrategy()
        infoLogger.addStrategy("memory", infoMemoryStrategy)
        
        infoLogger.debug("Debug message")
        infoLogger.info("Info message")
        infoLogger.warn("Warning message")
        
        val infoMemoryLogs = infoMemoryStrategy.getLogs()
        assertEquals(2, infoMemoryLogs.size)
    }
    
    @Test
    fun `should log with metadata`() {
        val metadata = mapOf("userId" to "123", "action" to "login")
        logger.info("User action", metadata = metadata)
        
        val memoryLogs = memoryStrategy.getLogs()
        assertEquals(1, memoryLogs.size)
        assertEquals(metadata, memoryLogs[0].metadata)
    }
    
    @Test
    fun `should log with exception`() {
        val exception = RuntimeException("Test exception")
        logger.error("Error occurred", throwable = exception)
        
        val memoryLogs = memoryStrategy.getLogs()
        assertEquals(1, memoryLogs.size)
        assertEquals(exception, memoryLogs[0].throwable)
    }
    
    @Test
    fun `should provide convenience methods for all log levels`() {
        logger.debug("Debug message")
        logger.info("Info message")
        logger.warn("Warning message")
        logger.error("Error message")
        logger.fatal("Fatal message")
        
        val memoryLogs = memoryStrategy.getLogs()
        assertEquals(4, memoryLogs.size)
        
        val levels = memoryLogs.map { it.level }
        assertTrue(levels.contains(LogLevel.INFO))
        assertTrue(levels.contains(LogLevel.WARN))
        assertTrue(levels.contains(LogLevel.ERROR))
        assertTrue(levels.contains(LogLevel.FATAL))
    }
    
    @Test
    fun `should get specific strategy`() {
        val retrievedStrategy = logger.getStrategy("memory")
        
        assertNotNull(retrievedStrategy)
        assertEquals("Memory", retrievedStrategy?.getName())
        assertTrue(retrievedStrategy is MemoryLoggingStrategy)
    }
    
    @Test
    fun `should return null for non-existent strategy`() {
        val retrievedStrategy = logger.getStrategy("non-existent")
        
        assertNull(retrievedStrategy)
    }
    
    @Test
    fun `should handle strategy failures gracefully`() {
        val failingStrategy = object : LoggingStrategy {
            override fun log(entry: LogEntry) {
                throw RuntimeException("Strategy failed")
            }
            
            override fun isAvailable(): Boolean = true
            
            override fun getName(): String = "FailingStrategy"
        }
        
        logger.addStrategy("failing", failingStrategy)
        
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val originalOut = System.out
        
        try {
            System.setOut(printStream)
            logger.info("Test message", strategy = "failing")
            
            val memoryLogs = memoryStrategy.getLogs()
            assertEquals(0, memoryLogs.size)
            
            val output = outputStream.toString()
            assertTrue(output.contains("Test message"))
        } finally {
            System.setOut(originalOut)
        }
    }
    
    @Test
    fun `should shutdown gracefully`() {
        logger.shutdown()
        
        assertTrue(logger.getAvailableStrategies().isEmpty())
    }
    
    @Test
    fun `should test memory strategy functionality`() {
        val memory = MemoryLoggingStrategy(maxEntries = 5)
        
        val entry1 = LogEntry(LogLevel.INFO, "First message")
        val entry2 = LogEntry(LogLevel.ERROR, "Second message")
        
        memory.log(entry1)
        memory.log(entry2)
        
        assertEquals(2, memory.getLogCount())
        val logs = memory.getLogs()
        assertEquals(2, logs.size)
        assertEquals(entry1, logs[0])
        assertEquals(entry2, logs[1])
        
        val infoLogs = memory.getLogsByLevel(LogLevel.INFO)
        val errorLogs = memory.getLogsByLevel(LogLevel.ERROR)
        
        assertEquals(1, infoLogs.size)
        assertEquals(1, errorLogs.size)
        
        memory.clearLogs()
        assertEquals(0, memory.getLogCount())
    }
    
    @Test
    fun `should test console strategy`() {
        val console = ConsoleLoggingStrategy(useColors = false)
        
        assertEquals("Console", console.getName())
        assertTrue(console.isAvailable())
        
        val entry = LogEntry(LogLevel.INFO, "Test message", logger = "TestLogger")
        
        val outputStream = ByteArrayOutputStream()
        val printStream = PrintStream(outputStream)
        val originalOut = System.out
        
        try {
            System.setOut(printStream)
            console.log(entry)
            
            val output = outputStream.toString()
            assertTrue(output.contains("Test message"))
            assertTrue(output.contains("[INFO]"))
            assertTrue(output.contains("[TestLogger]"))
        } finally {
            System.setOut(originalOut)
        }
    }
    
    @Test
    fun `should test file system strategy`() {
        val fileSystem = FileSystemLoggingStrategy("test-logs", "test.log")
        
        assertEquals("FileSystem", fileSystem.getName())
        
        val entry = LogEntry(LogLevel.INFO, "Test file message")
        fileSystem.log(entry)
        
        val logFile = java.io.File("test-logs/test.log")
        assertTrue(logFile.exists())
        assertTrue(logFile.readText().contains("Test file message"))
        
        logFile.delete()
        logFile.parentFile?.delete()
    }
}

