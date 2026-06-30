package service;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.AuthController;
import kg.attractor.java.server.Utils;
import model.Book;
import model.Employee;
import model.EmployeeBooks;
import repository.BookRepository;
import repository.EmployeeRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class BookController {

    private final BookRepository bookRepository = new BookRepository();
    private final AuthController authController;
    private final EmployeeRepository employeeRepository;

    public BookController(AuthController authController, EmployeeRepository employeeRepository) {
        this.authController = authController;
        this.employeeRepository = employeeRepository;
    }

    public void showBookPage(HttpExchange exchange, Lesson44Server server) {
        Employee user = authController.getAuthorizedUser(exchange);
        if (user == null) {
            redirectTo(exchange, "/login");
            return;
        }

        Map<String, Object> data = new HashMap<>();
        data.put("books", bookRepository.findAll());
        server.renderTemplate(exchange, "Books.html", data);
    }

    public void showBookDetailsPage(HttpExchange exchange, Lesson44Server server) {
        Employee user = authController.getAuthorizedUser(exchange);
        if (user == null) {
            redirectTo(exchange, "/login");
            return;
        }

        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = Utils.parseUrlEncoded(query, "&");
        String bookId = params.get("id");

        Book book = bookRepository.findById(bookId);

        Map<String, Object> data = new HashMap<>();
        if (book != null) {
            data.put("book", book);
            server.renderTemplate(exchange, "book-info.html", data);
        } else {
            sendError(exchange, 404, "Book with ID [" + bookId + "] not found!");
        }
    }

    public void takeBook(HttpExchange exchange) {
        Employee user = authController.getAuthorizedUser(exchange);
        if (user == null) {
            redirectTo(exchange, "/login");
            return;
        }

        try {
            String rawBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            Map<String, String> params = Utils.parseUrlEncoded(rawBody, "&");
            String bookId = params.get("bookId");

            if (user.getBooks() == null) {
                user.setBooks(new EmployeeBooks());
            }

            if (user.getBooks().getCurrentBooks() != null && user.getBooks().getCurrentBooks().size() >= 2) {
                System.out.println("Отказ: Лимит книг (2 шт) превышен для " + user.getFullname());
                redirectTo(exchange, "/books");
                return;
            }

            if (bookId != null) {
                Employee.CurrentBook currentBook = new Employee.CurrentBook();

                user.getBooks().getCurrentBooks().add(currentBook);
                employeeRepository.save(user);
            }

            redirectTo(exchange, "/profile");
        } catch (Exception e) {
            e.printStackTrace();
            redirectTo(exchange, "/books");
        }
    }

    public void returnBook(HttpExchange exchange) {
        Employee user = authController.getAuthorizedUser(exchange);
        if (user == null) {
            redirectTo(exchange, "/login");
            return;
        }

        try {
            String rawBody = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines().collect(Collectors.joining("\n"));
            Map<String, String> params = Utils.parseUrlEncoded(rawBody, "&");
            String bookId = params.get("bookId");

            if (bookId != null && user.getBooks() != null) {
                Employee.CurrentBook found = user.getBooks().getCurrentBooks().stream()
                        .filter(b -> bookId.equals(b.getBookId()))
                        .findFirst().orElse(null);

                if (found != null) {
                    user.getBooks().getCurrentBooks().remove(found);

                    Employee.HistoryBook historyBook = new Employee.HistoryBook();

                    user.getBooks().getHistoryBooks().add(historyBook);

                    employeeRepository.save(user);
                }
            }
            redirectTo(exchange, "/profile");
        } catch (Exception e) {
            e.printStackTrace();
            redirectTo(exchange, "/profile");
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

    private void sendError(HttpExchange exchange, int status, String msg) {
        try {
            exchange.sendResponseHeaders(status, msg.getBytes().length);
            exchange.getResponseBody().write(msg.getBytes());
            exchange.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}