package repository;

import com.google.gson.Gson;
import model.Book;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class BookRepository {
    private List<Book> books = new ArrayList<>();

    public BookRepository() {
        try (FileReader reader = new FileReader("src/data/books.json")) {
            Book[] bookArray = new Gson().fromJson(reader, Book[].class);
            if (bookArray != null) {
                books = new ArrayList<>(Arrays.asList(bookArray));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Book> findAll() {
        return books;
    }

    public Book findById(String id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}