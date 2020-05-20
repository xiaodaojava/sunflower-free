package red.lixiang.tools.desktop.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import red.lixiang.tools.desktop.conf.RunParam;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
public class AlertTools {

    public  static void popDialog(String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("公众号:java技术大本营");
        alert.setHeaderText("请注意");
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static Alert confirmAlert(String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("公众号:java技术大本营");
        alert.setHeaderText("请注意");
        alert.setContentText(content);
        return alert;
    }
    public static void confirmAlert(String content, Consumer<Optional<ButtonType>> consumer){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("公众号:java技术大本营");
        alert.setHeaderText("请注意");
        alert.setContentText(content);
        Optional<ButtonType> buttonType = alert.showAndWait();
        consumer.accept(buttonType);
    }

}
