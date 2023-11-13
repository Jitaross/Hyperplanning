module fr.atlas.hyperplanning{
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.logging;
    requires javafaker;
    requires java.sql;

    opens fr.atlas.hyperplanning.fxapp1 to javafx.fxml;
    exports fr.atlas.hyperplanning.fxapp1;
    exports fr.atlas.hyperplanning.fxapp2;
    opens fr.atlas.hyperplanning.fxapp2 to javafx.fxml;
    exports fr.atlas.hyperplanning.fxapp2.view;
    opens fr.atlas.hyperplanning.fxapp2.view to javafx.fxml;
    exports fr.atlas.hyperplanning.fxapp2.model;
    opens fr.atlas.hyperplanning.fxapp2.model to javafx.fxml;

}