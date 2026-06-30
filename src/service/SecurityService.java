package service;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.server.AuthController;
import model.Employee;
import java.io.IOException;

public class SecurityService {

    public static boolean isNotAuthorized(HttpExchange exchange, AuthController authController) {
        Employee user = authController.getAuthorizedUser(exchange);

        if (user == null) {
            try {
                exchange.getResponseHeaders().set("Location", "/login");
                exchange.sendResponseHeaders(303, -1);
                exchange.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        }

        return false;
    }
}