/**
 * @author lixiang
 * @date 2020/2/20
 **/
module red.lixiang.tools.desktopmain {
    requires com.google.gson;
    requires fastjson;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires javafx.web;
    requires javafx.graphics;
    requires sunshine.common;
    requires sunshine.base;
    requires java.sql;
    requires sshj;
    requires jdk.jsobject;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;

    opens red.lixiang.tools.desktop to javafx.fxml;
    opens red.lixiang.tools.desktop.controller.kub to javafx.fxml,javafx.base;
    opens red.lixiang.tools.desktop.controller.host to javafx.fxml;
    opens red.lixiang.tools.desktop.controller.set to javafx.fxml;
    opens red.lixiang.tools.desktop.controller.redis to javafx.fxml,javafx.base;
    opens red.lixiang.tools.desktop.controller.image to javafx.fxml;
    opens red.lixiang.tools.desktop.conf to fastjson;
    opens red.lixiang.tools.desktop.utils.notification.notification to javafx.fxml;
    opens red.lixiang.tools.desktop.controller.common to javafx.fxml;
    opens red.lixiang.tools.desktop.controller.ssh to javafx.fxml,javafx.base,fastjson;
    opens red.lixiang.tools.desktop.controller.sql to javafx.fxml,javafx.base;
    opens red.lixiang.tools.desktop.controller.study to javafx.fxml;
    opens red.lixiang.tools.desktop.controller.ssh.terminal to
            javafx.fxml,javafx.web,com.fasterxml.jackson.databind,com.fasterxml.jackson.annotation;


    exports red.lixiang.tools.desktop;
    exports red.lixiang.tools.desktop.controller;
    exports red.lixiang.tools.desktop.conf;
    exports red.lixiang.tools.desktop.controller.ssh to fastjson;


}