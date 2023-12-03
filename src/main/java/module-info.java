    module fr.utln.atlas.hyperplanning{
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.logging;
    requires javafaker;
    requires java.sql;
    requires commons.dbcp2;
    requires com.h2database;
    requires java.management;

		opens fr.utln.atlas.projethyp to javafx.fxml;
    exports fr.utln.atlas.projethyp;
    opens fr.utln.atlas.projethyp.controller to javafx.fxml;
    exports fr.utln.atlas.projethyp.controller;


}