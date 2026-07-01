package service;

import model.Employee;

public class AuthService {
    private static Employee currentUser = null;

    public static void login(Employee employee) {
        currentUser = employee;
    }

    public static void logout() {
        currentUser = null;
    }

    public static Employee getCurrentUser() {
        return currentUser;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }
}
