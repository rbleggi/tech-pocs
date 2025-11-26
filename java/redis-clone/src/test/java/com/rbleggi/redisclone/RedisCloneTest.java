package com.rbleggi.redisclone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RedisCloneTest {
    private RedisStore store;
    private CommandInvoker invoker;

    @BeforeEach
    void setUp() {
        store = new RedisStore();
        invoker = new CommandInvoker(store);
    }

    @Test
    void testSetAndGet() {
        assertEquals("OK", invoker.execute(new SetCommand("key1", "value1")));
        assertEquals("value1", invoker.execute(new GetCommand("key1")));
    }

    @Test
    void testGetNonExistentKey() {
        assertEquals("(nil)", invoker.execute(new GetCommand("nonexistent")));
    }

    @Test
    void testAppend() {
        assertEquals("OK", invoker.execute(new SetCommand("key1", "Hello")));
        assertEquals("Hello World", invoker.execute(new AppendCommand("key1", " World")));
        assertEquals("Hello World", invoker.execute(new GetCommand("key1")));
    }

    @Test
    void testAppendToNonExistentKey() {
        assertEquals("NewValue", invoker.execute(new AppendCommand("newkey", "NewValue")));
        assertEquals("NewValue", invoker.execute(new GetCommand("newkey")));
    }

    @Test
    void testRemove() {
        assertEquals("OK", invoker.execute(new SetCommand("key1", "value1")));
        assertEquals("OK", invoker.execute(new RemoveCommand("key1")));
        assertEquals("(nil)", invoker.execute(new GetCommand("key1")));
    }

    @Test
    void testRemoveNonExistentKey() {
        assertEquals("(nil)", invoker.execute(new RemoveCommand("nonexistent")));
    }

    @Test
    void testMapSetAndGet() {
        assertEquals("OK", invoker.execute(new MapSetCommand("map1", "field1", "value1")));
        assertEquals("value1", invoker.execute(new MapGetCommand("map1", "field1")));
    }

    @Test
    void testMapGetNonExistent() {
        assertEquals("(nil)", invoker.execute(new MapGetCommand("nonexistent", "field1")));
    }

    @Test
    void testMapKeys() {
        assertEquals("OK", invoker.execute(new MapSetCommand("map1", "a", "1")));
        assertEquals("OK", invoker.execute(new MapSetCommand("map1", "b", "2")));
        String keys = invoker.execute(new MapKeysCommand("map1"));
        assertTrue(keys.contains("a"));
        assertTrue(keys.contains("b"));
    }

    @Test
    void testMapValues() {
        assertEquals("OK", invoker.execute(new MapSetCommand("map1", "a", "1")));
        assertEquals("OK", invoker.execute(new MapSetCommand("map1", "b", "2")));
        String values = invoker.execute(new MapValuesCommand("map1"));
        assertTrue(values.contains("1"));
        assertTrue(values.contains("2"));
    }

    @Test
    void testMapKeysNonExistent() {
        assertEquals("(nil)", invoker.execute(new MapKeysCommand("nonexistent")));
    }

    @Test
    void testMapValuesNonExistent() {
        assertEquals("(nil)", invoker.execute(new MapValuesCommand("nonexistent")));
    }
}
