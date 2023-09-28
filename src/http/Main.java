package http;

public class Main {

    public static final int PORT = 8080;
    public static final int THREADS = 8080;

    public static void main(String[] args) {
        new HttpServer(PORT, THREADS);
    }
}