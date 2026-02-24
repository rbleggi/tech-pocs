package com.rbleggi.ownstringimpl;

import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) {
        System.out.println("Own String Impl");
    }

    private static String[] toStringArray(char[] chars) {
        var result = new String[chars.length];
        for (int i = 0; i < chars.length; i++) {
            result[i] = String.valueOf(chars[i]);
        }
        return result;
    }

    private static String iteratorToString(Iterator<Character> it) {
        var sb = new StringBuilder();
        while (it.hasNext()) {
            sb.append(it.next());
        }
        return sb.toString();
    }
}

class MyString {
    private final String value;

    MyString(String value) {
        this.value = value;
    }

    char[] toArray() {
        var arr = new char[value.length()];
        for (int i = 0; i < value.length(); i++) {
            arr[i] = value.charAt(i);
        }
        return arr;
    }

    void forEach(Consumer<Character> f) {
        for (int i = 0; i < value.length(); i++) {
            f.accept(value.charAt(i));
        }
    }

    MyString reverse() {
        var arr = new char[value.length()];
        for (int i = 0; i < value.length(); i++) {
            arr[i] = value.charAt(value.length() - 1 - i);
        }
        return new MyString(new String(arr));
    }

    Iterator<Character> iterator() {
        return new Iterator<>() {
            private int i = 0;

            @Override
            public boolean hasNext() {
                return i < value.length();
            }

            @Override
            public Character next() {
                return value.charAt(i++);
            }
        };
    }

    int length() {
        return value.length();
    }

    char charAt(int idx) {
        return value.charAt(idx);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof MyString myStr) {
            if (value.length() != myStr.value.length()) return false;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) != myStr.value.charAt(i)) return false;
            }
            return true;
        } else if (other instanceof String str) {
            if (value.length() != str.length()) return false;
            for (int i = 0; i < value.length(); i++) {
                if (value.charAt(i) != str.charAt(i)) return false;
            }
            return true;
        }
        return false;
    }

    boolean isEmpty() {
        return value.length() == 0;
    }

    MyString replace(char oldChar, char newChar) {
        var arr = new char[value.length()];
        for (int i = 0; i < value.length(); i++) {
            arr[i] = value.charAt(i) == oldChar ? newChar : value.charAt(i);
        }
        return new MyString(new String(arr));
    }

    MyString substring(int start, int end) {
        var arr = new char[end - start];
        for (int i = start; i < end; i++) {
            arr[i - start] = value.charAt(i);
        }
        return new MyString(new String(arr));
    }

    MyString trim() {
        var start = 0;
        var end = value.length() - 1;
        while (start <= end && (value.charAt(start) == ' ' || value.charAt(start) == '\t' || value.charAt(start) == '\n')) start++;
        while (end >= start && (value.charAt(end) == ' ' || value.charAt(end) == '\t' || value.charAt(end) == '\n')) end--;
        return start > end ? new MyString("") : substring(start, end + 1);
    }

    String toJson() {
        var sb = new StringBuilder();
        sb.append("{\"value\":\"");
        for (int i = 0; i < value.length(); i++) {
            var c = value.charAt(i);
            if (c == '"' || c == '\\') sb.append('\\');
            sb.append(c);
        }
        sb.append("\"}");
        return sb.toString();
    }

    int indexOf(char c) {
        for (int i = 0; i < value.length(); i++) {
            if (value.charAt(i) == c) return i;
        }
        return -1;
    }

    @Override
    public int hashCode() {
        var h = 0;
        for (int i = 0; i < value.length(); i++) {
            h = 31 * h + value.charAt(i);
        }
        return h;
    }

    @Override
    public String toString() {
        return value;
    }

    static MyString create(String s) {
        return new MyString(s);
    }
}
