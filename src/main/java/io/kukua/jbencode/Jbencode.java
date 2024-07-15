package io.kukua.jbencode;

import io.kukua.jbencode.exception.InvalidInputException;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.*;

public class Jbencode {

    private String encode(Long input) {
        return String.format("i%de", input);
    }

    private String encode(String input) {
        return String.format("%d:%s", input.length(), input);
    }

    private String encode(List<Object> input) {
        StringBuilder sb = new StringBuilder("l");
        for (Object o : input) {
            sb.append(encode(o));
        }
        sb.append("e");
        return sb.toString();
    }

    private String encode(SortedMap<String, Object> input) {
        StringBuilder sb = new StringBuilder("d");
        for (Map.Entry<String, Object> entry : input.entrySet()) {
            sb.append(encode(entry.getKey()));
            sb.append(encode(entry.getValue()));
        }
        sb.append("e");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public <T> String encode(T input) throws InvalidInputException {
        return switch (input) {
            case Long l -> encode(l);
            case String s -> encode(s);
            case List<?> list -> encode((List<Object>) list);
            case SortedMap<?, ?> map -> encode((SortedMap<String, Object>) map);
            case null, default -> throw new InvalidInputException();
        };
    }

    private Long decodeInteger(CharacterIterator iterator) {
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

    private String decodeString(CharacterIterator iterator) {
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

    private List<Object> decodeList(CharacterIterator iterator) {
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

    private SortedMap<String, Object> decodeMap(CharacterIterator iterator) {
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

    private Object decode(CharacterIterator iterator) {
        return switch (iterator.current()) {
            case 'i' -> decodeInteger(iterator);
            case '0','1','2','3','4','5','6','7','8','9' -> decodeString(iterator);
            case 'l' -> decodeList(iterator);
            case 'd' -> decodeMap(iterator);
            default -> throw new InvalidInputException();
        };
    }

    public Object decode(String input) throws InvalidInputException {
        return decode(new StringCharacterIterator(input));
    }

}
