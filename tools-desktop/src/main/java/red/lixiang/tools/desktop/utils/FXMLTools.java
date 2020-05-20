package red.lixiang.tools.desktop.utils;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.controller.ssh.SSHController;
import red.lixiang.tools.desktop.controller.ssh.terminal.TerminalController;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * @author lixiang
 * @date 2020/5/3
 **/
public class FXMLTools {

    /**
     * 初始化一个stage
     *
     * @param fxmlPath fxml的位置
     * @param <T>
     * @return
     */
    public static <T>  FXMLLoader initLoader(String fxmlPath){
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(FXMLTools.class.getResource(fxmlPath));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fxmlLoader;
    }

    public static Stage initStage(Parent root, Double width ,
                                  Double height){
        Scene scene = new Scene(root,width,height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        return stage;
    }
    public static Stage createStage(String fxmlPath, Double width ,
                                  Double height){
        FXMLLoader loader = initLoader(fxmlPath);
        Scene scene = new Scene(loader.getRoot(),width,height);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setMinWidth(width);
        stage.setMinHeight(height);
        return stage;
    }
}
