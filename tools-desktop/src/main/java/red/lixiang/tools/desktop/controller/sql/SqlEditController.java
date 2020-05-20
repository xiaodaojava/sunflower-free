package red.lixiang.tools.desktop.controller.sql;

import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import red.lixiang.tools.desktop.controller.ssh.SSHConfig;
import red.lixiang.tools.jdk.sql.SqlConfig;

import java.util.function.Consumer;

/**
 * @author lixiang
 * @date 2020/4/7
 **/
public class SqlEditController {
    public TextField tagField;
    public TextField usernameField;
    public PasswordField passwordField;
    public TextField urlField;
    public TextField portField;

    Consumer<SqlConfig> consumer;

    public void init(Consumer<SqlConfig> configConsumer){
        consumer = configConsumer;
    }

    public void save(ActionEvent actionEvent) {
        String url = urlField.getText();
        String password = passwordField.getText();
        String username = usernameField.getText();
        String tag = tagField.getText();
        SqlConfig config = new SqlConfig();
        config.setUrl(url)
                .setPort(Integer.valueOf(portField.getText()))
                .setPassword(password)
                .setUsername(username)
                .setTag(tag);
        consumer.accept(config);
    }
}
