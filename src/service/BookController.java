package service;

import com.sun.net.httpserver.HttpExchange;
import kg.attractor.java.lesson44.Lesson44Server;
import kg.attractor.java.server.Utils;
import model.Book;
import repository.BookRepository;

import java.util.HashMap;
import java.util.Map;

public class BookController {

    private final BookRepository bookRepository = new BookRepository();

    public void showBookPage(HttpExchange exchange, Lesson44Server server){

        Map<String, Object> data = new HashMap<>();

        data.put("books", bookRepository.findAll());

        server.renderTemplate(exchange,"Books.html", data);


    }

    public void showBookDetailsPage(HttpExchange exchange, Lesson44Server server) {
        String query = exchange.getRequestURI().getQuery();
        Map<String, String> params = Utils.parseUrlEncoded(query, "&");
        String bookId = params.get("id");

        Book book = bookRepository.findById(bookId);

        Map<String, Object> data = new HashMap<>();
        if (book != null) {
            data.put("book", book);
            server.renderTemplate(exchange, "book-info.html", data);
        } else {
            String notFound = "Книга не найдена!";
            try {
                exchange.sendResponseHeaders(404, notFound.getBytes().length);
                exchange.getResponseBody().write(notFound.getBytes());
                exchange.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
