package repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.Employee;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private String dataDir = "src/data";
    private String fileName = "employee.json";
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private List<Employee> employees = new ArrayList<>();

    public EmployeeRepository(){
        loadEmployee();
    }

    private void loadEmployee(){
        Path path = Paths.get(dataDir, fileName);
        if (!Files.exists(path)){
            System.out.println("Файл " + fileName + " не найден, инициализирован пустой список.");
            this.employees = new ArrayList<>();
            return;
        }

        try(Reader reader = Files.newBufferedReader(path)){
            employees = gson.fromJson(reader, new TypeToken<List<Employee>>(){}.getType());
            if (employees == null) {
                employees = new ArrayList<>();
            }
        } catch (IOException e) {
            System.out.println("Ошибка при чтении файла сотрудников: " + e.getMessage());
            employees = new ArrayList<>();
        }
    }

    public void saveAll(){
        Path path = Paths.get(dataDir, fileName);
        try (Writer writer = Files.newBufferedWriter(path)){
            gson.toJson(employees, writer);
        } catch (IOException e){
            System.out.println("Ошибка при записи Employee: " + e.getMessage());
        }
    }

    public List<Employee> findAll(){
        return employees;
    }

    public Employee findEmployee(String id){
        for(Employee employee : employees){
            if (employee.getIdEmployee().equals(id)){
                return employee;
            }
        }
        return null;
    }

    public Employee findByEmail(String email) {
        for (Employee employee : employees) {
            if (employee.getEmail() != null && employee.getEmail().equalsIgnoreCase(email)) {
                return employee;
            }
        }
        return null;
    }


}