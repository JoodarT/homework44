package model;

import java.util.List;

public class Employee {
    private String id;
    private String fullName;
    private List<CurrentBook> currentBooks;
    private List<HistoryBook> historyBooks;
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Employee(String id, String fullName, List<CurrentBook> currentBooks, List<HistoryBook> historyBooks) {
        this.id = id;
        this.fullName = fullName;
        this.currentBooks = currentBooks;
        this.historyBooks = historyBooks;
        this.email = id;
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

    public List<CurrentBook> getCurrentBooks() {
        return currentBooks;
    }

    public List<HistoryBook> getHistoryBooks() {
        return historyBooks;
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