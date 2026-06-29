package model;

import java.util.List;

public class EmployeeBooks {
    private List<Employee.CurrentBook> currentBooks;
    private List<Employee.HistoryBook> historyBooks;

    public void setHistoryBooks(List<Employee.HistoryBook> historyBooks) {
        this.historyBooks = historyBooks;
    }

    public void setCurrentBooks(List<Employee.CurrentBook> currentBooks) {
        this.currentBooks = currentBooks;
    }

    public List<Employee.HistoryBook> getHistoryBooks() {
        return historyBooks;
    }

    public List<Employee.CurrentBook> getCurrentBooks() {
        return currentBooks;
    }
}
