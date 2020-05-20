package red.lixiang.tools.desktop.controller.ssh;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author lixiang
 * @date 2020/4/1
 **/
public class SSHEditController {

    public TextField ipField;
    public PasswordField passwordField;
    public TextField usernameField;
    public TextField tagField;

    Consumer<SSHConfig> consumer;

    public void init(Consumer<SSHConfig> configConsumer){
        consumer = configConsumer;
    }

    public void save(ActionEvent actionEvent) {
        String ip = ipField.getText();
        String password = passwordField.getText();
        String username = usernameField.getText();
        String tag = tagField.getText();
        SSHConfig config = new SSHConfig();
        config.setIp(ip)
                .setPassword(password)
                .setUsername(username)
                .setTag(tag)
                .setAuthType(SSHConfig.AUTH_PASSWORD);
        consumer.accept(config);
    }
}
