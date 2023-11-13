module fr.utln.atlas.hyperplanning{
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.logging;
    requires javafaker;
    requires java.sql;

    opens fr.utln.atlas.projethyp.planningview to javafx.fxml;
    exports fr.utln.atlas.projethyp.planningview;
    exports fr.utln.atlas.projethyp.fxapp2;
    opens fr.utln.atlas.projethyp.fxapp2 to javafx.fxml;
    exports fr.utln.atlas.projethyp.fxapp2.view;
    opens fr.utln.atlas.projethyp.fxapp2.view to javafx.fxml;
    exports fr.utln.atlas.projethyp.fxapp2.model;
    opens fr.utln.atlas.projethyp.fxapp2.model to javafx.fxml;

}