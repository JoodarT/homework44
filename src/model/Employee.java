package model;

import java.util.List;

public class Employee {
    private String id;
    private String fullName;
    private String email;
    private String password;
    private EmployeeBooks books;

    public Employee() {
    }

    public EmployeeBooks getBooks() {
        return books;
    }

    public void setBooks(EmployeeBooks books) {
        this.books = books;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIdEmployee() {
        return id;
    }

    public String getFullname() {
        return fullName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullname(String name) {
        this.fullName = name;
    }

    public static class CurrentBook {
        private String bookId;
        private String issuedDate;

        public String getBookId() { return bookId; }
        public String getIssuedDate() { return issuedDate; }
    }

    public static class HistoryBook {
        private String bookId;
        private String issuedDate;
        private String returnedDate;

        public String getBookId() { return bookId; }
        public String getIssuedDate() { return issuedDate; }
        public String getReturnedDate() { return returnedDate; }
    }
}