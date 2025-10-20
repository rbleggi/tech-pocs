package com.rbleggi.fileshare

interface FileStorage {
    fun upload(file: File)
    fun download(id: String): File?
    fun delete(id: String)
    fun list(): List<File>
    fun search(query: String): List<File>
}

class EncryptedFileStorage : FileStorage {
    private val files = mutableMapOf<String, File>()
    override fun upload(file: File) { files[file.name] = file }
    override fun download(id: String) = files[id]
    override fun delete(id: String) { files.remove(id) }
    override fun list() = files.values.toList()
    override fun search(query: String) = files.values.filter { it.name.contains(query) }
}

object FileStorageFactory {
    fun create(type: String): FileStorage = when(type) {
        "encrypted" -> EncryptedFileStorage()
        else -> throw IllegalArgumentException("Unknown type")
    }
}

interface FileEventObserver {
    fun onEvent(event: FileEvent)
}

class LoggingObserver : FileEventObserver {
    override fun onEvent(event: FileEvent) {
        println("Event: ${event.type} - ${event.file?.name}")
    }
}

data class File(val name: String, val content: String = "")
data class FileEvent(val type: String, val file: File?)

class FileShareSystem(private val storage: FileStorage) {
    private val observers = mutableListOf<FileEventObserver>()
    fun addObserver(observer: FileEventObserver) { observers.add(observer) }
    fun removeObserver(observer: FileEventObserver) { observers.remove(observer) }
    private fun notify(event: FileEvent) { observers.forEach { it.onEvent(event) } }
    fun upload(file: File) {
        storage.upload(file)
        notify(FileEvent("upload", file))
    }
    fun download(id: String): File? {
        val file = storage.download(id)
        notify(FileEvent("download", file))
        return file
    }
    fun delete(id: String) {
        val file = storage.download(id)
        storage.delete(id)
        notify(FileEvent("delete", file))
    }
    fun list(): List<File> = storage.list()
    fun search(query: String): List<File> = storage.search(query)
}

fun main() {
    val system = FileShareSystem(FileStorageFactory.create("encrypted"))
    system.addObserver(LoggingObserver())
    system.upload(File("report.pdf", "content"))
    println(system.list())
    println(system.search("report"))
    system.delete("report.pdf")
}

