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
    private String dataDir = "data";
    private String fileName = "books.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private List<Book> books = new ArrayList<>();

    public Repository(){
        loadBooks();
    }

    private void loadBooks(){
        Path path = Paths.get(dataDir, fileName);
        if (!Files.exists(path)){
            System.out.printf("Файл   " + fileName + "не найден, инициализирован пустой список.");

            this.books = new ArrayList<>();

            return;
        }

        try(Reader reader = Files.newBufferedReader(path)){

            books = gson.fromJson(reader, new TypeToken<List<Book>>(){}.getType());

        }catch (IOException e) {
            System.out.printf("Ошибка при чтении файла книг" + e.getMessage());
            books = new ArrayList<>();
        }

    }
    public void savedAll(){
        Path path = Paths.get(dataDir, fileName);

        try (Writer writer = (Files.newBufferedWriter(path))){

            gson.toJson(books, writer);
        }catch (IOException e){
            System.out.printf("Ошибка при записи книг "  + e.getMessage());
        }

    }




}
