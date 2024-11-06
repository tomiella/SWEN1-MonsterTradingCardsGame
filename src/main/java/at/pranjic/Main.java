package at.pranjic;

import at.pranjic.server.http.HttpMethod;
import at.pranjic.server.http.Request;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Request req = new Request();
        req.setMethod(HttpMethod.GET);

    }

}