package io.kukua.jbencode;

import io.kukua.jbencode.exception.InvalidInputException;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

public class Jbencode {

    static private String encode(Long input) {
        return String.format("i%de", input);
    }

    static private String encode(String input) {
        return String.format("%d:%s", input.length(), input);
    }

    static private String encode(List<Object> input) {
        StringBuilder sb = new StringBuilder("l");
        for (Object o : input) {
            sb.append(encode(o));
        }
        sb.append("e");
        return sb.toString();
    }

    static private String encode(SortedMap<String, Object> input) {
        StringBuilder sb = new StringBuilder("d");
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            sb.append(encode(entry.getKey()));
            sb.append(encode(entry.getValue()));
        }
        sb.append("e");
        return sb.toString();
    }

    /**
     * Encodes parameter according to <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a> specifications.
     * Encoding is done recursively in case of {@link List} or {@link SortedMap}.
     *
     * @param input The value to be encoded.
     *              Must be of type {@link Long}, {@link String}, {@link List} Or {@link SortedMap}.
     * @return The encoded value.
     * @throws InvalidInputException Thrown when invalid type is given as parameter.
     */
    @SuppressWarnings("unchecked")
    static public <T> String encode(T input) throws InvalidInputException {
        return switch (input) {
            case Long l -> encode(l);
            case String s -> encode(s);
            case List<?> list -> encode((List<Object>) list);
            case SortedMap<?, ?> map -> encode((SortedMap<String, Object>) map);
            case null, default -> throw new InvalidInputException();
        };
    }

    static private Long decodeLong(CharacterIterator iterator) {
        StringBuilder sb = new StringBuilder();
        // move the cursor next to the delimiter 'i'
        iterator.next();
        while (iterator.current() != CharacterIterator.DONE && iterator.current() != 'e') {
            sb.append(iterator.current());
            iterator.next();
        }
        // end delimiter 'e' is missing
        if (iterator.current() != 'e') {
            throw new InvalidInputException();
        }
        // move the cursor to the next encoded value or end of iterator
        iterator.next();
        try {
            return Long.parseLong(sb.toString());
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    static private String decodeString(CharacterIterator iterator) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(iterator.current());
        } while (iterator.current() != CharacterIterator.DONE && iterator.next() != ':');
        try {
            int stringLength = Integer.parseInt(sb.toString());
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < stringLength; i++) {
                char nextChar = iterator.next();
                // string length is invalid
                if (nextChar == CharacterIterator.DONE) {
                    throw new InvalidInputException();
                }
                result.append(nextChar);
            }
            // move the cursor to the next encoded value or end of iterator
            iterator.next();
            return result.toString();
        } catch (NumberFormatException e) {
            throw new InvalidInputException();
        }
    }

    static private List<Object> decodeList(CharacterIterator iterator) {
        List<Object> result = new ArrayList<>();
        // move the cursor next to the delimiter 'l'
        iterator.next();
        do {
            // case of empty list
            if (iterator.current() != 'e') {
                result.add(decode(iterator));
            }
        } while (iterator.current() != 'e');
        // move the cursor to the next encoded value or end of iterator
        iterator.next();
        return result;
    }

    static private SortedMap<String, Object> decodeMap(CharacterIterator iterator) {
        SortedMap<String, Object> result = new TreeMap<>();
        // move the cursor next to the delimiter 'd'
        iterator.next();
        do {
            // case of empty map
            if (iterator.current() != 'e') {
                result.put(decodeString(iterator), decode(iterator));
            }
        } while (iterator.current() != 'e');
        // move the cursor to the next encoded value or end of iterator
        iterator.next();
        return result;
    }

    static private Object decode(CharacterIterator iterator) {
        return switch (iterator.current()) {
            case 'i' -> decodeLong(iterator);
            case '0','1','2','3','4','5','6','7','8','9' -> decodeString(iterator);
            case 'l' -> decodeList(iterator);
            case 'd' -> decodeMap(iterator);
            default -> throw new InvalidInputException();
        };
    }

    /**
     * Decodes the string according to <a href="https://en.wikipedia.org/wiki/Bencode">Bencode</a> specifications.
     * Returns the first possible decoded value and ignores all subsequent ones.
     *
     * @param input The value to be decoded.
     * @return The decoded {@link Long}, {@link String}, {@link List} Or {@link SortedMap} value.
     * @throws InvalidInputException Thrown when invalid encoded value is given as parameter.
     */
    static public Object decode(String input) throws InvalidInputException {
        return decode(new StringCharacterIterator(input));
    }

}
