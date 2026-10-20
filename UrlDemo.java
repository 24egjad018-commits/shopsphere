package com.shopsphere.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class UrlDemo {

    public static void main(String[] args) throws Exception {
        partsOfAUrl();
        readOverHttp();
        readFromFile();
    }

    // 1. Break a URL into its parts
    private static void partsOfAUrl() throws Exception {
        URL url = URI.create("http://localhost:8080/web/products?lang=hi").toURL();

        System.out.println("--- Parts of a URL ---");
        System.out.println("Protocol: " + url.getProtocol());
        System.out.println("Host:     " + url.getHost());
        System.out.println("Port:     " + url.getPort());
        System.out.println("Path:     " + url.getPath());
        System.out.println("Query:    " + url.getQuery());
    }

    // 2. Read our own web page over HTTP (Tomcat must be running)
    private static void readOverHttp() {
        System.out.println("--- Reading a web page over HTTP ---");
        try {
            URL url = URI.create("http://localhost:8080/web/products").toURL();
            URLConnection connection = url.openConnection();   // the http protocol handler is chosen here
            HttpURLConnection http = (HttpURLConnection) connection;
            http.setConnectTimeout(3000);
            http.setReadTimeout(3000);

            System.out.println("Handler class: " + connection.getClass().getName());
            System.out.println("Status:        " + http.getResponseCode());
            System.out.println("Content type:  " + http.getContentType());

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("<title>")) {
                        System.out.println("Title line:    " + line.trim());
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Could not read the page. Is Tomcat started? (" + e.getMessage() + ")");
        }
    }

    // 3. Read a local file through the file: protocol
    private static void readFromFile() throws Exception {
        System.out.println("--- Reading a local file with the file: protocol ---");

        Path temp = Files.createTempFile("shopsphere-demo", ".txt");
        Files.writeString(temp, "Hello from a file URL\n");

        try {
            URL url = temp.toUri().toURL();
            URLConnection connection = url.openConnection();

            System.out.println("Handler class: " + connection.getClass().getName());
            System.out.println("Content type:  " + connection.getContentType());

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                System.out.println("First line:    " + reader.readLine());
            }

            // The content handler for text/plain decides what object getContent() returns
            Object content = url.getContent();
            System.out.println("getContent():  " + content.getClass().getName());
            if (content instanceof InputStream in) {
                in.close();
            }
        } finally {
            Files.deleteIfExists(temp);   // clean up the temporary file
        }
    }
}