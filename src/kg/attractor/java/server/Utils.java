package kg.attractor.java.server;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class Utilis {
    public static Map<String, String> parserUrlEncodet(String rawLines, String delimiter){
        String[] pairs = rawLines.split(delimiter);
        Stream<Map.Entry<String, String>> stream = Arrays.stream(pairs)
                
    }
}
