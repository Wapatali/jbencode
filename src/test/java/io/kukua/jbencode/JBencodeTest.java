package io.kukua.jbencode;

import io.kukua.jbencode.exception.InvalidInputException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    @Test
    @DisplayName("Decode integer should return decoded integer")
    public void decode_integer_should_return_decoded_integer() {
        assertEquals(0L, JBENCODE.decode("i0e"));
        assertEquals(10L, JBENCODE.decode("i10e"));
        assertEquals(-10L, JBENCODE.decode("i-10e"));
    }

    @Test
    @DisplayName("Decode string should return decoded string")
    public void decode_string_should_return_decoded_string() {
        assertEquals("", JBENCODE.decode("0:"));
        assertEquals("bob", JBENCODE.decode("3:bob"));
        assertEquals("alice", JBENCODE.decode("5:alice"));
    }

    @Test
    @DisplayName("Decode list should return decoded list")
    public void decode_list_should_return_decoded_list() {
        assertEquals(emptyList(), JBENCODE.decode("le"));
        assertEquals(List.of(10L), JBENCODE.decode("li10ee"));
        assertEquals(List.of("bob"), JBENCODE.decode("l3:bobe"));
        assertEquals(List.of(emptySortedMap()), JBENCODE.decode("ldee"));
    }

    @Test
    @DisplayName("Decode map should return decoded map")
    public void decode_map_should_return_decoded_map() {
        assertEquals(emptySortedMap(), JBENCODE.decode("de"));
        assertEquals(new TreeMap<>(Map.of("integer", 10L)), JBENCODE.decode("d7:integeri10ee"));
        assertEquals(new TreeMap<>(Map.of("string", "bob")), JBENCODE.decode("d6:string3:bobe"));
        assertEquals(new TreeMap<>(Map.of("list", emptyList())), JBENCODE.decode("d4:listlee"));
    }

    @Test
    @DisplayName("Decode with invalid input should throw InvalidInputException")
    public void decode_with_invalid_input_should_throw_exception() {
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("ie"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("10e"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("i10"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("ibobe"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("3:"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode(":bob"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("bob"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("l"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("d"));
        assertThrows(InvalidInputException.class, () -> JBENCODE.decode("e"));
    }

}
