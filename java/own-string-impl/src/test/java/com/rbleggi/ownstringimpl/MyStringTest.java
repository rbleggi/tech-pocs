package com.rbleggi.ownstringimpl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MyStringTest {

    @Test
    @DisplayName("MyString toArray should return character array")
    void myString_toArray_returnsCharArray() {
        var str = new MyString("Hello");
        char[] arr = str.toArray();
        assertEquals(5, arr.length);
        assertEquals('H', arr[0]);
        assertEquals('o', arr[4]);
    }

    @Test
    @DisplayName("MyString forEach should iterate over all characters")
    void myString_forEach_iteratesAllCharacters() {
        var str = new MyString("ABC");
        var result = new StringBuilder();
        str.forEach(result::append);
        assertEquals("ABC", result.toString());
    }

    @Test
    @DisplayName("MyString reverse should reverse the string")
    void myString_reverse_reversesString() {
        var str = new MyString("Hello");
        var reversed = str.reverse();
        assertEquals("olleH", reversed.toString());
    }

    @Test
    @DisplayName("MyString iterator should iterate through characters")
    void myString_iterator_iteratesCharacters() {
        var str = new MyString("Test");
        var it = str.iterator();
        assertTrue(it.hasNext());
        assertEquals('T', it.next());
        assertEquals('e', it.next());
        assertEquals('s', it.next());
        assertEquals('t', it.next());
        assertFalse(it.hasNext());
    }

    @Test
    @DisplayName("MyString length should return string length")
    void myString_length_returnsCorrectLength() {
        var str = new MyString("Hello");
        assertEquals(5, str.length());
    }

    @Test
    @DisplayName("MyString charAt should return character at index")
    void myString_charAt_returnsCharAtIndex() {
        var str = new MyString("Hello");
        assertEquals('H', str.charAt(0));
        assertEquals('e', str.charAt(1));
        assertEquals('o', str.charAt(4));
    }

    @Test
    @DisplayName("MyString equals should compare with another MyString")
    void myString_equals_comparesMyStrings() {
        var str1 = new MyString("Hello");
        var str2 = new MyString("Hello");
        var str3 = new MyString("World");
        assertTrue(str1.equals(str2));
        assertFalse(str1.equals(str3));
    }

    @Test
    @DisplayName("MyString equals should compare with Java String")
    void myString_equals_comparesWithString() {
        var str = new MyString("Hello");
        assertTrue(str.equals("Hello"));
        assertFalse(str.equals("World"));
    }

    @Test
    @DisplayName("MyString isEmpty should return true for empty string")
    void myString_isEmpty_returnsTrueForEmpty() {
        var empty = new MyString("");
        var notEmpty = new MyString("Hello");
        assertTrue(empty.isEmpty());
        assertFalse(notEmpty.isEmpty());
    }

    @Test
    @DisplayName("MyString replace should replace all occurrences")
    void myString_replace_replacesAllOccurrences() {
        var str = new MyString("Hello");
        var replaced = str.replace('l', 'x');
        assertEquals("Hexxo", replaced.toString());
    }

    @Test
    @DisplayName("MyString substring should extract substring")
    void myString_substring_extractsSubstring() {
        var str = new MyString("Hello World");
        var sub = str.substring(0, 5);
        assertEquals("Hello", sub.toString());
    }

    @Test
    @DisplayName("MyString trim should remove leading and trailing whitespace")
    void myString_trim_removesWhitespace() {
        var str = new MyString("  Hello  ");
        var trimmed = str.trim();
        assertEquals("Hello", trimmed.toString());
    }

    @Test
    @DisplayName("MyString trim should handle all whitespace")
    void myString_trim_handlesAllWhitespace() {
        var str = new MyString("   ");
        var trimmed = str.trim();
        assertEquals("", trimmed.toString());
    }

    @Test
    @DisplayName("MyString toJson should escape special characters")
    void myString_toJson_escapesSpecialCharacters() {
        var str = new MyString("Hello \"World\"");
        var json = str.toJson();
        assertTrue(json.contains("\\\""));
    }

    @Test
    @DisplayName("MyString indexOf should find character index")
    void myString_indexOf_findsCharacter() {
        var str = new MyString("Hello");
        assertEquals(1, str.indexOf('e'));
        assertEquals(4, str.indexOf('o'));
        assertEquals(-1, str.indexOf('z'));
    }

    @Test
    @DisplayName("MyString hashCode should return consistent hash")
    void myString_hashCode_returnsConsistentHash() {
        var str1 = new MyString("Hello");
        var str2 = new MyString("Hello");
        assertEquals(str1.hashCode(), str2.hashCode());
    }

    @Test
    @DisplayName("MyString toString should return string value")
    void myString_toString_returnsStringValue() {
        var str = new MyString("Hello");
        assertEquals("Hello", str.toString());
    }

    @Test
    @DisplayName("MyString create should create MyString instance")
    void myString_create_createsInstance() {
        var str = MyString.create("Hello");
        assertNotNull(str);
        assertEquals("Hello", str.toString());
    }
}
