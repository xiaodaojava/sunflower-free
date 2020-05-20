package red.lixiang.tools.desktop.manager.set;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.lixiang.tools.desktop.conf.FxBean;

import java.io.IOException;

/**
 * @author lixiang
 * @date 2020/3/24
 **/
@FxBean
public class SetManager {

    public Label idLabel;

    public Button loginBtn;

    public ImageView userHead;



    public SetManager setIdLabel(Label idLabel) {
        this.idLabel = idLabel;
        return this;
    }

    public SetManager setLoginBtn(Button loginBtn) {
        this.loginBtn = loginBtn;
        return this;
    }

    public SetManager setUserHead(ImageView userHead) {
        this.userHead = userHead;
        return this;
    }
}
