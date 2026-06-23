package model;

public class Book {
    private String id;
    private String title;
    private String author;
    private String image;
    private String status;
    private String currentHolderId;

    public Book(String id, String title, String author, String image, String status, String currentHolderId) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.image = image;
        this.status = status;
        this.currentHolderId = currentHolderId;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public String getStatus() {
        return status;
    }

    public String getCurrentHolderId() {
        return currentHolderId;
    }
}