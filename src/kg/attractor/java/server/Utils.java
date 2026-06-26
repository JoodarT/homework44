package kg.attractor.java.server;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static Map<String, String> parseUrlEncoded(String rawLines, String delimiter) {
        if (rawLines == null || rawLines.isBlank()) {
            return Map.of();
        }

        return Arrays.stream(rawLines.split(delimiter))
                .map(pair -> pair.split("=", 2))
                .filter(keyValue -> keyValue.length > 0)
                .collect(Collectors.toMap(
                        keyValue -> decode(keyValue[0]),
                        keyValue -> keyValue.length > 1 ? decode(keyValue[1]) : "",
                        (existing, replacement) -> replacement
                ));
    }

    private static String decode(String value) {
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }
}