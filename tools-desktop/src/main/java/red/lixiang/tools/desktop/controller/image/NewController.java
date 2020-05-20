package red.lixiang.tools.desktop.controller.image;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import red.lixiang.tools.common.github.GitConfig;
import red.lixiang.tools.jdk.StringTools;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * @author lixiang
 * @date 2020/5/3
 **/
public class NewController implements Initializable {

    public ToggleGroup gitTypeGroup = new ToggleGroup();


    public PasswordField accessTokenField;
    public RadioButton githubRadio;
    public RadioButton giteeRadio;
    public TextField ownerField;
    public TextField repoField;
    public TextField proxyField;
    public TextField portField;
    public TextField tagField;

    private Consumer<GitConfig> afterSaveConfig;

    private Stage currentStage;


    public void init(Stage stage,Consumer<GitConfig> configConsumer){
        afterSaveConfig = configConsumer;
        currentStage = stage;
    }


    /**
     * 保存配置
     * @param actionEvent
     */
    public void saveConfig(ActionEvent actionEvent) {
        GitConfig config = getConfig();
        afterSaveConfig.accept(config);
        currentStage.close();
    }

    private GitConfig getConfig() {
        GitConfig config = new GitConfig();
        String owner = ownerField.getText();
        String repo = repoField.getText();
        if (StringTools.isNotBlank(accessTokenField.getText())) {
            String token = accessTokenField.getText();
            config.setPersonToken(token);
        }
        String proxyUrl = proxyField.getText();
        String proxyPort = portField.getText();
        String tag = tagField.getText();
        config.setOwner(owner).setRepo(repo);
        if (StringTools.isNotBlank(proxyPort)) {
            config.setProxyUrl(proxyUrl).setProxyPort(Integer.valueOf(proxyPort));
        }
        RadioButton selectedToggle = (RadioButton) gitTypeGroup.getSelectedToggle();
        Integer type = "github".equals(selectedToggle.getText()) ? 0 : 1;
        config.setGitType(type);
        config.setTag(tag);
        return config;
    }

    public void renderConfig(GitConfig config) {
        if (config == null) {
            return;
        }
        if(config.getGitType()!=null){
            githubRadio.setSelected(config.getGitType() == 0);
            giteeRadio.setSelected(config.getGitType() == 1);
        }
        ownerField.setText(config.getOwner());
        repoField.setText(config.getRepo());
        proxyField.setText(config.getProxyUrl());
        tagField.setText(config.getTag());
        if (config.getProxyPort() != null) {
            portField.setText(String.valueOf(config.getProxyPort()));
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        githubRadio.setToggleGroup(gitTypeGroup);
        giteeRadio.setToggleGroup(gitTypeGroup);
    }
}
