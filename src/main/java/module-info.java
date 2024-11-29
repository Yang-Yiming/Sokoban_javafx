module Sokoban {
    requires javafx.base;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;

    opens org.view.menu to javafx.fxml;
    opens org.view.level to javafx.fxml;
    exports org;
}