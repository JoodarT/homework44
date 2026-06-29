package kg.attractor.java.server;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;

import java.util.HashMap;
import java.util.Map;

public class CookieController {
    public void lesson46Handler(HttpExchange exchange, Lesson44Server server) {
        Map<String, Object> data = new HashMap<>();
        int times = 42;
        data.put("times", times);
        server.renderTemplate(exchange, "cookie.html", data);
    }

    Cookie sessionCookie = Cookie.make("userId", "123");

    
}


