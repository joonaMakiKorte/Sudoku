module com.example.sudokuguifx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sudokuguifx to javafx.fxml;
    exports com.example.sudokuguifx;
}