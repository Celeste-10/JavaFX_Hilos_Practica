module ni.edu.uam.practicahilosjavafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens controller to javafx.fxml;
    opens ni.edu.uam.practicahilosjavafx to javafx.fxml;
    exports ni.edu.uam.practicahilosjavafx;
}