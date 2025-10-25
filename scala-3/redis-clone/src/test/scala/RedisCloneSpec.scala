package com.rbleggi.redisclone

class RedisCloneSpec {
  test("SetCommand should store value and return OK") {
    val store = new RedisStore()
    val cmd = SetCommand("key1", "value1")
    val result = cmd.execute(store)

    assert(result == "+OK")
    assert(store.strings("key1") == "value1")
  }

  test("GetCommand should retrieve stored value") {
    val store = new RedisStore()
    store.strings("key1") = "value1"
    val cmd = GetCommand("key1")

    assert(cmd.execute(store) == "value1")
  }

  test("GetCommand should return nil for non-existent key") {
    val store = new RedisStore()
    val cmd = GetCommand("nonexistent")

    assert(cmd.execute(store) == "(nil)")
  }

  test("RemoveCommand should delete key and return OK") {
    val store = new RedisStore()
    store.strings("key1") = "value1"
    val cmd = RemoveCommand("key1")

    assert(cmd.execute(store) == "+OK")
    assert(!store.strings.contains("key1"))
  }

  test("RemoveCommand should return nil for non-existent key") {
    val store = new RedisStore()
    val cmd = RemoveCommand("nonexistent")

    assert(cmd.execute(store) == "(nil)")
  }

  test("AppendCommand should append to existing value") {
    val store = new RedisStore()
    store.strings("key1") = "hello"
    val cmd = AppendCommand("key1", " world")

    val result = cmd.execute(store)
    assert(result == "hello world")
    assert(store.strings("key1") == "hello world")
  }

  test("AppendCommand should create new key if not exists") {
    val store = new RedisStore()
    val cmd = AppendCommand("key1", "hello")

    assert(cmd.execute(store) == "hello")
  }

  test("MapSetCommand should store map field-value pair") {
    val store = new RedisStore()
    val cmd = MapSetCommand("map1", "field1", "value1")

    assert(cmd.execute(store) == "+OK")
    assert(store.maps("map1")("field1") == "value1")
  }

  test("MapGetCommand should retrieve map field value") {
    val store = new RedisStore()
    import scala.collection.mutable
    store.maps("map1") = mutable.Map("field1" -> "value1")
    val cmd = MapGetCommand("map1", "field1")

    assert(cmd.execute(store) == "value1")
  }

  test("MapGetCommand should return nil for non-existent field") {
    val store = new RedisStore()
    val cmd = MapGetCommand("map1", "field1")

    assert(cmd.execute(store) == "(nil)")
  }

  test("MapKeysCommand should return all keys in map") {
    val store = new RedisStore()
    import scala.collection.mutable
    store.maps("map1") = mutable.Map("field1" -> "value1", "field2" -> "value2")
    val cmd = MapKeysCommand("map1")

    val result = cmd.execute(store)
    assert(result.contains("field1"))
    assert(result.contains("field2"))
  }

  test("MapValuesCommand should return all values in map") {
    val store = new RedisStore()
    import scala.collection.mutable
    store.maps("map1") = mutable.Map("field1" -> "value1", "field2" -> "value2")
    val cmd = MapValuesCommand("map1")

    val result = cmd.execute(store)
    assert(result.contains("value1"))
    assert(result.contains("value2"))
  }
}

