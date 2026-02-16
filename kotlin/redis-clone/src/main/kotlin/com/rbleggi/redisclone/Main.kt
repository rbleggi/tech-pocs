package com.rbleggi.redisclone

interface Command {
    fun execute(store: RedisStore): String
}

class RedisStore {
    val strings = mutableMapOf<String, String>()
    val maps = mutableMapOf<String, MutableMap<String, String>>()
}

class SetCommand(private val key: String, private val value: String) : Command {
    override fun execute(store: RedisStore): String {
        store.strings[key] = value
        return "OK"
    }
}

class GetCommand(private val key: String) : Command {
    override fun execute(store: RedisStore): String {
        return store.strings[key] ?: "(nil)"
    }
}

class RemoveCommand(private val key: String) : Command {
    override fun execute(store: RedisStore): String {
        return if (store.strings.remove(key) != null) "OK" else "(nil)"
    }
}

class AppendCommand(private val key: String, private val value: String) : Command {
    override fun execute(store: RedisStore): String {
        val newValue = (store.strings[key] ?: "") + value
        store.strings[key] = newValue
        return newValue
    }
}

class MapSetCommand(private val map: String, private val key: String, private val value: String) : Command {
    override fun execute(store: RedisStore): String {
        val m = store.maps.getOrPut(map) { mutableMapOf() }
        m[key] = value
        return "OK"
    }
}

class MapGetCommand(private val map: String, private val key: String) : Command {
    override fun execute(store: RedisStore): String {
        return store.maps[map]?.get(key) ?: "(nil)"
    }
}

class MapKeysCommand(private val map: String) : Command {
    override fun execute(store: RedisStore): String {
        return store.maps[map]?.keys?.joinToString(", ") ?: "(nil)"
    }
}

class MapValuesCommand(private val map: String) : Command {
    override fun execute(store: RedisStore): String {
        return store.maps[map]?.values?.joinToString(", ") ?: "(nil)"
    }
}

class CommandInvoker(private val store: RedisStore) {
    fun execute(command: Command): String = command.execute(store)
}

fun main() {
    val store = RedisStore()
    val invoker = CommandInvoker(store)
    println(invoker.execute(SetCommand("foo", "bar")))
    println(invoker.execute(GetCommand("foo")))
    println(invoker.execute(AppendCommand("foo", "baz")))
    println(invoker.execute(RemoveCommand("foo")))
    println(invoker.execute(GetCommand("foo")))
    println(invoker.execute(MapSetCommand("myMap", "a", "1")))
    println(invoker.execute(MapSetCommand("myMap", "b", "2")))
    println(invoker.execute(MapGetCommand("myMap", "a")))
    println(invoker.execute(MapKeysCommand("myMap")))
    println(invoker.execute(MapValuesCommand("myMap")))
}
