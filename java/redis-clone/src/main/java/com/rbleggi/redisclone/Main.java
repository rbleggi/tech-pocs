package com.rbleggi.redisclone;

import java.util.HashMap;
import java.util.Map;

interface Command {
    String execute(RedisStore store);
}

class RedisStore {
    final Map<String, String> strings = new HashMap<>();
    final Map<String, Map<String, String>> maps = new HashMap<>();
}

record SetCommand(String key, String value) implements Command {
    @Override
    public String execute(RedisStore store) {
        store.strings.put(key, value);
        return "OK";
    }
}

record GetCommand(String key) implements Command {
    @Override
    public String execute(RedisStore store) {
        return store.strings.getOrDefault(key, "(nil)");
    }
}

record RemoveCommand(String key) implements Command {
    @Override
    public String execute(RedisStore store) {
        return store.strings.remove(key) != null ? "OK" : "(nil)";
    }
}

record AppendCommand(String key, String value) implements Command {
    @Override
    public String execute(RedisStore store) {
        String newValue = store.strings.getOrDefault(key, "") + value;
        store.strings.put(key, newValue);
        return newValue;
    }
}

record MapSetCommand(String map, String key, String value) implements Command {
    @Override
    public String execute(RedisStore store) {
        store.maps.computeIfAbsent(map, k -> new HashMap<>()).put(key, value);
        return "OK";
    }
}

record MapGetCommand(String map, String key) implements Command {
    @Override
    public String execute(RedisStore store) {
        Map<String, String> m = store.maps.get(map);
        return m != null ? m.getOrDefault(key, "(nil)") : "(nil)";
    }
}

record MapKeysCommand(String map) implements Command {
    @Override
    public String execute(RedisStore store) {
        Map<String, String> m = store.maps.get(map);
        return m != null ? String.join(", ", m.keySet()) : "(nil)";
    }
}

record MapValuesCommand(String map) implements Command {
    @Override
    public String execute(RedisStore store) {
        Map<String, String> m = store.maps.get(map);
        return m != null ? String.join(", ", m.values()) : "(nil)";
    }
}

class CommandInvoker {
    private final RedisStore store;

    CommandInvoker(RedisStore store) {
        this.store = store;
    }

    String execute(Command command) {
        return command.execute(store);
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("Redis Clone");
    }
}
