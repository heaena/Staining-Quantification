module image.analysis.cloud.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires mybatis.plus.core;
    requires spring.core;
    requires mybatis.plus.annotation;
    requires lombok;
    requires org.apache.commons.lang3;
    requires mybatis.plus;
    requires fastjson;
    requires spring.context;
    requires java.annotation;
    requires spring.web;
    requires org.slf4j;
    requires java.desktop;
    requires spring.webmvc;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires spring.boot.autoconfigure;
    requires spring.boot;
    requires org.mybatis;
    requires spring.beans;
    requires mybatis.plus.extension;

    opens image.analysis.cloud.app.entrypoint.gui to javafx.fxml;
    exports image.analysis.cloud.app;
}
