package kg.attractor.java.server;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.Utils;
import model.Employee;
import repository.EmployeeRepository;
import service.AuthService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthController {
    private final EmployeeRepository employeeRepository;

    public AuthController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void profilePage(HttpExchange exchange, Lesson44Server server) {
        Map<String, Object> data = new HashMap<>();

        if (AuthService.isLoggedIn()) {
            Employee user = AuthService.getCurrentUser();
            data.put("name", user.getFullname());
            data.put("email", user.getEmail());
        } else {
            data.put("name", "Некий пользователь");
            data.put("email", "unknown@mail.com");
        }

        server.renderTemplate(exchange, "profile.html", data);
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

                AuthService.login(employee);

                exchange.getResponseHeaders().set("Location", "/profile");
                exchange.sendResponseHeaders(303, -1);
                exchange.close();
            } else {
                System.out.println("Login failed for: " + email);
                exchange.getResponseHeaders().set("Location", "/login");
                exchange.sendResponseHeaders(303, -1);
                exchange.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void registerPage(HttpExchange exchange, Lesson44Server server) {
        java.util.Map<String, Object> data = new java.util.HashMap<>();
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
                Employee newEmployee = new Employee(
                        email,
                        name,
                        new java.util.ArrayList<>(),
                        new java.util.ArrayList<>()
                );

                newEmployee.setEmail(email);
                newEmployee.setPassword(password);

                employeeRepository.save(newEmployee);

                data.put("success", "Удачная регистрация!");
                server.renderTemplate(exchange, "register.html", data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}