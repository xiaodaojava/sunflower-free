package red.lixiang.tools.desktop.controller.set;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.lixiang.tools.base.BaseResponse;
import red.lixiang.tools.common.ding.DingConfig;
import red.lixiang.tools.common.ding.DingTools;
import red.lixiang.tools.desktop.bean.BeanMap;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.manager.set.SetManager;
import red.lixiang.tools.desktop.utils.AlertTools;
import red.lixiang.tools.jdk.StringTools;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *
 *
 * @author lixiang
 * @date 2020/2/29
 **/
public class SetController implements Initializable {
    public ImageView javaSubscribe;
    public ImageView whisperMini;

    public ImageView userHead;
    public Label idLabel;
    public Button loginBtn;

    public AnchorPane settingPane;

    public TextArea feedText;


    SetManager setManager = BeanMap.getBean(SetManager.class);

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setManager.setIdLabel(idLabel).setLoginBtn(loginBtn).setUserHead(userHead);
        InputStream javaIs = SetController.class.getResourceAsStream("/tools/icon/java_subscribe.jpg");
        javaSubscribe.setImage(new Image(javaIs));
        InputStream whisperIs = SetController.class.getResourceAsStream("/tools/icon/couxin.jpeg");
        whisperMini.setImage(new Image(whisperIs));
    }



    /**
     * 提交反馈
     * @param actionEvent
     */
    public void feedback(ActionEvent actionEvent) {
        String content;
        if(StringTools.isBlank(content = feedText.getText())){
            return;
        }
        DingConfig config = new DingConfig();
        config.setSecret("SEC17a6fabdfee0817e520ae1cd45bbbf8ecb1476911b5db6d93e1cdb70600df075");
        config.setUrl("https://oapi.dingtalk.com/robot/send?access_token=f5217f00e524caf0a7590848d0dfba35cd08920d5ade715e1eea888ff3962a82");
        DingTools.sendText("Sunflower反馈信息:"+content,config);
        feedText.setText("");
    }

    /**
     * 支持作者的二维码弹框
     * @param actionEvent
     */
    public void support(ActionEvent actionEvent) throws IOException {
        Stage loginStage = new Stage();
        RunParam.RunParam().setLoginStage(loginStage);
        loginStage.setTitle("感谢您的支持");
        //modality要使用Modality.APPLICATION_MODEL
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setMinWidth(600);
        loginStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/set/support.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root, 600, 500);
        loginStage.setScene(scene);
        loginStage.showAndWait();
    }

    private Integer versionNum(String version){
        return Arrays.stream(version.split("\\.")).mapToInt(Integer::parseInt).sum();
    }
}
