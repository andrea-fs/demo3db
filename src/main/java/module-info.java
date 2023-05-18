module com.code.demo3db {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;

    opens com.code.demo3db to javafx.fxml;
    exports com.code.demo3db;
}