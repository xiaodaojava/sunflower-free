package red.lixiang.tools.desktop.controller.common;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.lixiang.tools.desktop.conf.RunParam;

import java.io.IOException;

/**
 * @author lixiang
 * @date 2020/3/28
 **/
public class CommonController {

    public TextField q;

    public void translator(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        RunParam.RunParam().setKubEditStage(editStage);
        editStage.setTitle("翻译(百度翻译)");
        //modality要使用Modality.APPLICATION_MODEL
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.setMinWidth(600);
        editStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/common/translator.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,600,500);
        editStage.setScene(scene);
        editStage.showAndWait();
    }


    public void transform(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        RunParam.RunParam().setKubEditStage(editStage);
        editStage.setTitle("各种转换");
        //modality要使用Modality.APPLICATION_MODEL
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.setMinWidth(600);
        editStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/common/transform.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,600,500);
        editStage.setScene(scene);
        editStage.showAndWait();


    }

    public void searchMaven(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        RunParam.RunParam().setKubEditStage(editStage);
        editStage.setTitle("maven依赖搜索");
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.setMinWidth(600);
        editStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/common/webview.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,600,500);
        WebviewController controller = fxmlLoader.getController();
        String url = "https://mvnrepository.com/search?q="+q.getText();
        controller.init(url);
        editStage.setScene(scene);
        editStage.showAndWait();
    }
}
