package red.lixiang.tools.desktop.manager.kub;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import red.lixiang.tools.common.kubernetes.KuberTools;
import red.lixiang.tools.common.kubernetes.KubernetesConfig;
import red.lixiang.tools.common.kubernetes.models.Pod;
import red.lixiang.tools.desktop.conf.FxBean;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.controller.kub.KubLogController;
import red.lixiang.tools.desktop.controller.kub.KubPodCell;
import red.lixiang.tools.desktop.utils.AlertTools;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
@FxBean
public class KubManager {

    public GlobalConfig globalConfig = GlobalConfig.getConfig();

    private ToggleGroup kubTagGroup = new ToggleGroup();

    public FlowPane kubTagPane;

    private List<Pod> currentPods = null;

    public KubernetesConfig getTargetConfig() {
        Toggle selectedToggle = kubTagGroup.getSelectedToggle();
        RadioButton toggle = (RadioButton) selectedToggle;
        String tag = toggle.getText();
        List<KubernetesConfig> configList = globalConfig.getKubConfigList();
        KubernetesConfig target = null;
        for (KubernetesConfig config : configList) {
            if (config.getTag().equals(tag)) {
                target = config;
                break;
            }
        }
        if (target == null) {
            return null;
        }
        return target;
    }


    public List<Pod> searchPods(String searchName) {

        return KuberTools.getAllPods(searchName, getTargetConfig());

    }

    public void delPod(KubPodCell podCell){
        Pod currentPod = new Pod();
        currentPod.setName(podCell.getName())
                .setNamespace(podCell.getNamespace());
        String s = KuberTools.deletePos(currentPod, getTargetConfig());

    }

    public void showLogStage(KubPodCell podCell) {

        Pod currentPod = new Pod();
        currentPod.setName(podCell.getName())
                .setNamespace(podCell.getNamespace());
        //有多少个容器,就开多少个窗口
        Stage logStage = new Stage();
        logStage.setTitle("日志:" + currentPod.getName());
        //modality要使用Modality.APPLICATION_MODEL会禁止响应,WINDOW_MODAL可以响应别的点击
        logStage.initModality(Modality.WINDOW_MODAL);
        logStage.setMinWidth(800);
        logStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/kub/logs.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        KubLogController logController = fxmlLoader.getController();
        logController.init(currentPod,getTargetConfig());
       // logController.showLog(podLogs);
        Scene scene = new Scene(root, 800, 500);
        logStage.setScene(scene);
        logStage.show();


    }


    public void editKubConfig(KubernetesConfig config) {
        ObservableList<Node> children = kubTagPane.getChildren();
        RadioButton button = new RadioButton(config.getTag());
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                ContextMenu contextMenu = new ContextMenu();
                MenuItem delete = new MenuItem("delete");
                delete.setOnAction(menuEvent -> {
                    children.remove(button);
                    // 从kubConfig中删除
                    globalConfig.getKubConfigList().removeIf(x -> x.getTag().equals(config.getTag()));
                });
                MenuItem cancel = new MenuItem("cancel");
                cancel.setOnAction(menuEvent -> {
                    contextMenu.hide();
                });
                contextMenu.getItems().add(delete);
                contextMenu.getItems().add(cancel);
                contextMenu.show(RunParam.RunParam().getKubPane(), event.getScreenX(), event.getScreenY());
            }
        });
        button.setToggleGroup(kubTagGroup);
        children.add(button);
    }


    public KubManager setKubTagPane(FlowPane kubTagPane) {
        this.kubTagPane = kubTagPane;
        return this;
    }
}
