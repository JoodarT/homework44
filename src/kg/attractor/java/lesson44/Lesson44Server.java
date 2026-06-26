package kg.attractor.java.lesson44;

import com.sun.net.httpserver.HttpExchange;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import kg.attractor.java.server.AuthController;
import kg.attractor.java.server.BasicServer;
import kg.attractor.java.server.ContentType;
import kg.attractor.java.server.ResponseCodes;
import model.Book;
import model.Employee;
import repository.Repository;
import repository.EmployeeRepository;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Lesson44Server extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();

    private final Repository bookRepository = new Repository();
    private final EmployeeRepository employeeRepository = new EmployeeRepository();
    private final AuthController authController = new AuthController(employeeRepository);

    public Lesson44Server(String host, int port) throws IOException {
        super(host, port);

        registerGet("/register", exchange -> authController.registerPage(exchange, this));
        registerPost("/register", exchange -> authController.register(exchange, this));

        registerGet("/", this::loginPageHandler);
        registerGet("/index.html", this::loginPageHandler);
        registerGet("/login", this::loginPageHandler);

        registerPost("/login", authController::login);

        registerGet("/sample", this::freemarkerSampleHandler);
        registerGet("/books", this::booksHandler);
        registerGet("/employees", this::employeesHandler);

        registerGet("/css/forms.css", this::staticFilesHandler);
        registerGet("/images/1.jpg", this::staticFilesHandler);

        registerGet("/profile", exchange -> authController.profilePage(exchange, this));
    }

    protected final void registerPost(String route, kg.attractor.java.server.RouteHandler handler) {
        getRoutes().put("POST " + route, handler);
    }

    private void staticFilesHandler(HttpExchange httpExchange) {
        try {
            String pathStr = "templates" + httpExchange.getRequestURI().getPath();
            java.nio.file.Path path = java.nio.file.Paths.get(pathStr);

            if (java.nio.file.Files.exists(path)) {
                byte[] data = java.nio.file.Files.readAllBytes(path);

                ContentType contentType = ContentType.TEXT_PLAIN;
                if (pathStr.endsWith(".css")) {
                    contentType = ContentType.TEXT_CSS;
                } else if (pathStr.endsWith(".jpg") || pathStr.endsWith(".jpeg")) {
                    contentType = ContentType.IMAGE_JPEG;
                } else if (pathStr.endsWith(".png")) {
                    contentType = ContentType.IMAGE_PNG;
                }

                sendByteData(httpExchange, ResponseCodes.OK, contentType, data);
            } else {
                String notFound = "File not found";
                sendByteData(httpExchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_PLAIN, notFound.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginPageHandler(HttpExchange httpExchange) {
        renderTemplate(httpExchange, "index.html", new HashMap<>());
    }

    private void booksHandler(HttpExchange exchange) {
        List<Book> books = bookRepository.findAll();
        Map<String, Object> data = new HashMap<>();
        data.put("books", books);
        renderTemplate(exchange, "books.html", data);
    }

    private void employeesHandler(HttpExchange exchange) {
        List<Employee> employees = employeeRepository.findAll();
        Map<String, Object> data = new HashMap<>();
        data.put("employees", employees);
        renderTemplate(exchange, "employees.html", data);
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("templates"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void freemarkerSampleHandler(HttpExchange exchange) {
        renderTemplate(exchange, "sample.html", getSampleDataModel());
    }

    public void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }

    private SampleDataModel getSampleDataModel() {
        return new SampleDataModel();
    }
}