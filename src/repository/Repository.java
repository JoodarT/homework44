package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Book;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Repository {
    private String dataDir = "src/data";
    private String fileName = "books.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Book> books = new ArrayList<>();

    public Repository(){
        loadBooks();
    }

    private void loadBooks(){
        Path path = Paths.get(dataDir, fileName);
        if (!Files.exists(path)){
            System.out.println("Файл " + fileName + " не найден, инициализирован пустой список.");
            this.books = new ArrayList<>();
            return;
        }

        try(Reader reader = Files.newBufferedReader(path)){
            books = gson.fromJson(reader, new TypeToken<List<Book>>(){}.getType());
            if (books == null) {
                books = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла книг: " + e.getMessage());
            books = new ArrayList<>();
        }
    }

    public void saveAll(){
        Path path = Paths.get(dataDir, fileName);

        try (Writer writer = Files.newBufferedWriter(path)){
            gson.toJson(books, writer);
        } catch (IOException e){
            System.out.println("Ошибка при записи книг: " + e.getMessage());
        }
    }

    public List<Book> findAll(){
        return books;
    }

    public Book findBook(String id) {
        for (Book book : books) {
            if (book.getId().equals(id)) {
                return book;
            }
        }
        return null;
    }
}