package red.lixiang.tools.desktop.controller.redis;

import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import red.lixiang.tools.common.redis.RedisConfig;
import red.lixiang.tools.desktop.utils.AlertTools;
import red.lixiang.tools.jdk.StringTools;

import java.util.function.Consumer;

/**
 * @author lixiang
 * @date 2020/5/7
 **/
public class NewController {

    public TextField hostField;
    public TextField portField;
    public TextField passField;
    public TextField tagField;

    private Consumer<RedisConfig> configConsumer;

    private Stage curStage;

    public void init(Consumer<RedisConfig> consumer, Stage stage){
        this.configConsumer = consumer;
        curStage = stage;
    }


    public void saveConfig(ActionEvent actionEvent) {
        RedisConfig config = new RedisConfig();
        if (StringTools.isBlank(hostField.getText())) {
            AlertTools.popDialog("HOST必须填写");
            return;
        }
        config.setHost(hostField.getText());
        if (StringTools.isNotBlank(portField.getText())) {
            config.setPort(Integer.parseInt(portField.getText()));
        }
        config.setTag(StringTools.isBlank(tagField.getText()) ? hostField.getText() : tagField.getText());
        config.setPassword(StringTools.isBlank(passField.getText())?null:passField.getText());
        configConsumer.accept(config);
        curStage.close();
    }
}
