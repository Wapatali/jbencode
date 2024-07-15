package io.kukua.jbencode;

import io.kukua.jbencode.exception.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.emptySortedMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JBencodeTest {

    private static class Dummy {}
    private final Jbencode JBENCODE = new Jbencode();

    @Test
    @DisplayName("Encode integer should return encoded string")
    public void encode_integer_should_return_encoded_string() {
        assertEquals("i0e", JBENCODE.encode(0L));
        assertEquals("i10e", JBENCODE.encode(10L));
        assertEquals("i-10e", JBENCODE.encode(-10L));
    }

    @Test
    @DisplayName("Encode string should return encoded string")
    public void encode_string_should_return_encoded_string() {
        assertEquals("3:bob", JBENCODE.encode("bob"));
        assertEquals("5:alice", JBENCODE.encode("alice"));
    }

    @Test
    @DisplayName("Encode list should return encoded string")
    public void encode_list_should_return_encoded_string() {
        assertEquals("le", JBENCODE.encode(emptyList()));
        assertEquals("li10ee", JBENCODE.encode(List.of(10L)));
        assertEquals("l3:bobe", JBENCODE.encode(List.of("bob")));
        assertEquals("ldee", JBENCODE.encode(List.of(emptySortedMap())));
    }

    @Test
    @DisplayName("Encode map should return encoded string")
    public void encode_map_should_return_encoded_string() {
        assertEquals("de", JBENCODE.encode(emptySortedMap()));
        assertEquals("d7:integeri10ee", JBENCODE.encode(new TreeMap<>(Map.of("integer", 10L))));
        assertEquals("d6:string3:bobe", JBENCODE.encode(new TreeMap<>(Map.of("string", "bob"))));
        assertEquals("d4:listlee", JBENCODE.encode(new TreeMap<>(Map.of("list", emptyList()))));
    }

    @Test
    @DisplayName("Encode with invalid input type should throw InvalidInputException")
    public void encode_withInvalidInput_shouldThrowException() {
        assertThrows(InvalidInputException.class, () -> JBENCODE.encode(new Dummy()));
    }

}
