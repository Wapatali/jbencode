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

    @Test
    @DisplayName("Encode integer should return encoded string")
    public void encode_integer_should_return_encoded_string() {
        assertEquals("i0e", Jbencode.encode(0L));
        assertEquals("i10e", Jbencode.encode(10L));
        assertEquals("i-10e", Jbencode.encode(-10L));
    }

    @Test
    @DisplayName("Encode string should return encoded string")
    public void encode_string_should_return_encoded_string() {
        assertEquals("3:bob", Jbencode.encode("bob"));
        assertEquals("5:alice", Jbencode.encode("alice"));
    }

    @Test
    @DisplayName("Encode list should return encoded string")
    public void encode_list_should_return_encoded_string() {
        assertEquals("le", Jbencode.encode(emptyList()));
        assertEquals("li10ee", Jbencode.encode(List.of(10L)));
        assertEquals("l3:bobe", Jbencode.encode(List.of("bob")));
        assertEquals("ldee", Jbencode.encode(List.of(emptySortedMap())));
    }

    @Test
    @DisplayName("Encode map should return encoded string")
    public void encode_map_should_return_encoded_string() {
        assertEquals("de", Jbencode.encode(emptySortedMap()));
        assertEquals("d7:integeri10ee", Jbencode.encode(new TreeMap<>(Map.of("integer", 10L))));
        assertEquals("d6:string3:bobe", Jbencode.encode(new TreeMap<>(Map.of("string", "bob"))));
        assertEquals("d4:listlee", Jbencode.encode(new TreeMap<>(Map.of("list", emptyList()))));
    }

    @Test
    @DisplayName("Encode with invalid input type should throw InvalidInputException")
    public void encode_withInvalidInput_shouldThrowException() {
        assertThrows(InvalidInputException.class, () -> Jbencode.encode(new Dummy()));
    }

    @Test
    @DisplayName("Decode integer should return decoded integer")
    public void decode_integer_should_return_decoded_integer() {
        assertEquals(0L, Jbencode.decode("i0e"));
        assertEquals(10L, Jbencode.decode("i10e"));
        assertEquals(-10L, Jbencode.decode("i-10e"));
    }

    @Test
    @DisplayName("Decode string should return decoded string")
    public void decode_string_should_return_decoded_string() {
        assertEquals("", Jbencode.decode("0:"));
        assertEquals("bob", Jbencode.decode("3:bob"));
        assertEquals("alice", Jbencode.decode("5:alice"));
    }

    @Test
    @DisplayName("Decode list should return decoded list")
    public void decode_list_should_return_decoded_list() {
        assertEquals(emptyList(), Jbencode.decode("le"));
        assertEquals(List.of(10L), Jbencode.decode("li10ee"));
        assertEquals(List.of("bob"), Jbencode.decode("l3:bobe"));
        assertEquals(List.of(emptySortedMap()), Jbencode.decode("ldee"));
    }

    @Test
    @DisplayName("Decode map should return decoded map")
    public void decode_map_should_return_decoded_map() {
        assertEquals(emptySortedMap(), Jbencode.decode("de"));
        assertEquals(new TreeMap<>(Map.of("integer", 10L)), Jbencode.decode("d7:integeri10ee"));
        assertEquals(new TreeMap<>(Map.of("string", "bob")), Jbencode.decode("d6:string3:bobe"));
        assertEquals(new TreeMap<>(Map.of("list", emptyList())), Jbencode.decode("d4:listlee"));
    }

    @Test
    @DisplayName("Decode with invalid input should throw InvalidInputException")
    public void decode_with_invalid_input_should_throw_exception() {
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("ie"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("10e"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("i10"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("ibobe"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("3:"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode(":bob"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("bob"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("l"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("d"));
        assertThrows(InvalidInputException.class, () -> Jbencode.decode("e"));
    }

}
