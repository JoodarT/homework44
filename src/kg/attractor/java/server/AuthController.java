package kg.attractor.java.server;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.server.Utils;
import model.Employee;
import repository.EmployeeRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthController {
    private final EmployeeRepository employeeRepository;

    public AuthController(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void login(HttpExchange exchange) {
        try {
            String rawBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Map<String, String> params = Utils.parseUrlEncoded(rawBody, "&");
            String email = params.get("email");

            Employee employee = employeeRepository.findByEmail(email);

            if (employee != null) {
                System.out.println("Success login: " + employee.getFullname());
                exchange.getResponseHeaders().set("Location", "/books");
                exchange.sendResponseHeaders(303, -1);
                exchange.close();
            } else {
                System.out.println("User not found: " + email);
                exchange.getResponseHeaders().set("Location", "/login");
                exchange.sendResponseHeaders(303, -1);
                exchange.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}