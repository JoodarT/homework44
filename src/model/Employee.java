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

    public Employee(String id, String fullName, List<CurrentBook> currentBooks, List<HistoryBook> historyBooks) {
        this.id = id;
        this.fullName = fullName;
        this.currentBooks = currentBooks;
        this.historyBooks = historyBooks;
        this.email = email;
    }

    public String getIdEmployee() {
        return id;
    }

    public String getFullname() {
        return fullName;
    }

    public List<CurrentBook> getCurrentBooks() {
        return currentBooks;
    }

    public List<HistoryBook> getHistoryBooks() {
        return historyBooks;
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