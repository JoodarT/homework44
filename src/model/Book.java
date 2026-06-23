package model;

public class Book {
   private int id;
    private String author;
    private String image;
    private BookStatus status;
    private int idEmployee;
    private int currentHolderId;

    public Book(int id, String author,  String image, BookStatus status, int idEmployee, int currentHolderId) {
        this.id = id;
        this.author = author;
        this.image = image;
        this.status = status;
        this.idEmployee = idEmployee;
        this.currentHolderId = currentHolderId;

    }

    public int getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public BookStatus getStatus() {
        return status;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }


}
