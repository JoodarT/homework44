package model;

import java.util.List;

public class Employee {
    private int idEmployee;
    private String fullname;
    private String firstname;
    private List<Integer>currentBooks;
    private  List<Integer>historyBooks;

    public Employee(int idEmployee, String fullname, String firstname,
                    List<Integer> currentBooks, List<Integer> historyBooks) {
        this.idEmployee = idEmployee;
        this.fullname = fullname;
        this.firstname = firstname;


        this.currentBooks = currentBooks;
        this.historyBooks = historyBooks;
    }

    public int getIdEmployee() {
        return idEmployee;
    }

    public String getFullname() {
        return fullname;
    }

    public String getFirstname() {
        return firstname;
    }

    public List<Integer> getCurrentBooks() {
        return currentBooks;
    }

    public List<Integer> getHistoryBooks() {
        return historyBooks;
    }
}
