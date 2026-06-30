package kg.attractor.java.server;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import model.Employee;
import model.EmployeeBooks;
import repository.EmployeeRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class AuthController {
    private final EmployeeRepository employeeRepository;
    private final Map<String, Employee> activeSessions = new HashMap<>();

    public AuthController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void logout(HttpExchange exchange, Lesson44Server server) {
        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");

        if (cookieHeader != null && !cookieHeader.isBlank()) {
            Map<String, String> cookies = Utils.parseUrlEncoded(cookieHeader.replace("; ", "&"), "&");
            String sessionId = cookies.get("userId");

            if (sessionId != null) {
                activeSessions.remove(sessionId);
            }
        }

        Cookie<String> deleteCookie = Cookie.make("userId", "");
        deleteCookie.setMaxAge(0);
        deleteCookie.setHttpOnly(true);
        exchange.getResponseHeaders().add("Set-Cookie", deleteCookie.toString());

        redirectTo(exchange, "/login");
    }

    public void profilePage(HttpExchange exchange, Lesson44Server server) {
        Map<String, Object> data = new HashMap<>();
        Employee user = null;

        String cookieHeader = exchange.getRequestHeaders().getFirst("Cookie");

        if (cookieHeader != null && !cookieHeader.isBlank()) {
            Map<String, String> cookies = Utils.parseUrlEncoded(cookieHeader.replace("; ", "&"), "&");
            String sessionId = cookies.get("userId");

            if (sessionId != null) {
                user = activeSessions.get(sessionId);
            }
        }

        if (user != null) {
            data.put("name", user.getFullname());
            data.put("email", user.getEmail());

            if (user.getBooks() != null) {
                data.put("books", user.getBooks());
            }

            server.renderTemplate(exchange, "profile.html", data);
        } else {
            redirectTo(exchange, "/login");
        }
    }

    public void login(HttpExchange exchange) {
        try {
            String rawBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Map<String, String> params = Utils.parseUrlEncoded(rawBody, "&");
            String email = params.get("email");
            String password = params.get("password");

            Employee employee = employeeRepository.findByEmail(email);

            if (employee != null && password != null && password.equals(employee.getPassword())) {
                System.out.println("Success login: " + employee.getFullname());

                String sessionId = UUID.randomUUID().toString();
                activeSessions.put(sessionId, employee);

                Cookie<String> sessionCookie = Cookie.make("userId", sessionId);
                sessionCookie.setMaxAge(600);
                sessionCookie.setHttpOnly(true);

                exchange.getResponseHeaders().add("Set-Cookie", sessionCookie.toString());

                redirectTo(exchange, "/profile");
            } else {
                System.out.println("Login failed for: " + email);
                redirectTo(exchange, "/login");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerPage(HttpExchange exchange, Lesson44Server server) {
        Map<String, Object> data = new HashMap<>();
        server.renderTemplate(exchange, "register.html", data);
    }

    public void register(HttpExchange exchange, Lesson44Server server) {
        try {
            String rawBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Map<String, String> params = Utils.parseUrlEncoded(rawBody, "&");
            String email = params.get("email");
            String name = params.get("name");
            String password = params.get("password");

            Employee existing = employeeRepository.findByEmail(email);
            Map<String, Object> data = new HashMap<>();

            if (existing != null) {
                data.put("error", "Регистрация не удалась: пользователь с таким email уже существует!");
                server.renderTemplate(exchange, "register.html", data);
            } else {
                Employee newEmployee = new Employee();
                newEmployee.setEmail(email);
                newEmployee.setFullname(name);
                newEmployee.setPassword(password);
                newEmployee.setBooks(new EmployeeBooks());

                employeeRepository.save(newEmployee);

                data.put("success", "Удачная регистрация!");
                server.renderTemplate(exchange, "register.html", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void redirectTo(HttpExchange exchange, String location) {
        try {
            exchange.getResponseHeaders().set("Location", location);
            exchange.sendResponseHeaders(303, -1);
            exchange.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}