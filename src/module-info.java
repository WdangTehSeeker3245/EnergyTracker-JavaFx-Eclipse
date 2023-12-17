module MencobaJavaBiasaFXEclipse {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.media;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.swt;

    opens com.wdangtehseeker3245.energytracker to javafx.fxml;
    exports com.wdangtehseeker3245.energytracker;
}
