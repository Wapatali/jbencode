package io.kukua.jbencode;

import io.kukua.jbencode.exception.InvalidInputException;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

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

}
