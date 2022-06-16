module pw.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires java.desktop;
    requires org.jsoup;
    requires javafx.media;
    requires javafx.web;


    opens pw.client to javafx.fxml;
    exports pw.client;
}