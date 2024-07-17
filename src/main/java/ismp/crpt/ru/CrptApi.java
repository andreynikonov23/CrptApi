package ismp.crpt.ru;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class CrptApi {
    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost", 8000), 2);
        httpServer.createContext("/ismp.crpt.ru/api/", exchange -> {
            System.out.println("Hello World!");
        });
        httpServer.start();
    }
}