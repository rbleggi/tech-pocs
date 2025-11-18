package com.rbleggi.filesharesystem;

public record File(String name, String content, boolean isEncrypted) {
    public File(String name, String content) {
        this(name, content, false);
    }
}
