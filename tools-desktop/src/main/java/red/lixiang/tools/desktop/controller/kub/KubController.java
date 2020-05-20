package red.lixiang.tools.desktop.controller.kub;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import red.lixiang.tools.common.kubernetes.KuberTools;
import red.lixiang.tools.common.kubernetes.KubernetesConfig;
import red.lixiang.tools.common.kubernetes.models.Pod;
import red.lixiang.tools.desktop.bean.BeanMap;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.manager.kub.KubManager;
import red.lixiang.tools.desktop.utils.AlertTools;
import red.lixiang.tools.desktop.utils.TableTools;
import red.lixiang.tools.jdk.StringTools;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
public class KubController  implements Initializable {

    public GlobalConfig config =  GlobalConfig.getConfig();

    @FXML
    public AnchorPane kubPane;


    public FlowPane kubTagPane;

    public TextField podSearchField;


    public TableView<KubPodCell> kubTable;
    public TableColumn<KubPodCell,String> nameCol;
    public TableColumn<KubPodCell,String> namespaceCol;
    public TableColumn<KubPodCell,String> statusCol;
    public TableColumn optionCol;

    ObservableList<KubPodCell> podCellList;


    private KubManager kubManager;

    public void showEditModal(ActionEvent actionEvent) throws IOException {
        Stage editStage = new Stage();
        RunParam.RunParam().setKubEditStage(editStage);
        editStage.setTitle("请添加配置");
        //modality要使用Modality.APPLICATION_MODEL
        editStage.initModality(Modality.APPLICATION_MODAL);
        editStage.setMinWidth(600);
        editStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/kub/edit.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fxmlLoader.load();
        Scene scene = new Scene(root,600,500);
        editStage.setScene(scene);
        editStage.showAndWait();

    }



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
        kubManager = BeanMap.getBean(KubManager.class);
        kubTable.getSelectionModel().setCellSelectionEnabled(true);
        TableTools.installCopyPasteHandler(kubTable);
        kubTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        RunParam.RunParam().setKubPane(kubPane);
        kubManager.setKubTagPane(kubTagPane);
        List<KubernetesConfig> kubConfigList = config.getKubConfigList();
        for (KubernetesConfig kubernetesConfig : kubConfigList) {
            kubManager.editKubConfig(kubernetesConfig);
        }
        //为每个column绑定数据
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        namespaceCol.setCellValueFactory(new PropertyValueFactory<>("namespace"));
        optionCol.setCellFactory(param -> {
            TableCell<KubPodCell,String> cell  = new TableCell<>(){
                final Button delBtn = new Button("删除pod");
                final Button logBtn = new Button("日志");
                HBox hBox = new HBox(logBtn,delBtn);
                @Override
                protected void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        logBtn.setOnAction(event -> {
                            KubPodCell podCell = getTableView().getItems().get(getIndex());
                            kubManager.showLogStage(podCell);
                        });
                        delBtn.setOnAction(event -> {
                            AlertTools.confirmAlert("确定要删除?",x->{
                                if (x.get()== ButtonType.OK) {
                                    KubPodCell podCell = getTableView().getItems().get(getIndex());
                                    kubManager.delPod(podCell);
                                    AlertTools.popDialog("OK");
                                }
                            });

                        });
                        setGraphic(hBox);
                        setText(null);
                    }
                }
            };
            return cell;
        });
    }

    public void searchPods(ActionEvent actionEvent) {
        String  podSearchName = podSearchField.getText();
        if(StringTools.isBlank(podSearchName)){
            AlertTools.popDialog("请输入名称");
            return;
        }
        List<Pod> pods = kubManager.searchPods(podSearchName);
        List<KubPodCell> cellList = pods.stream().map(x -> {
            KubPodCell cell = new KubPodCell();
            cell.setName(x.getName());
            cell.setNamespace(x.getNamespace());
            return cell;
        }).collect(Collectors.toList());
        podCellList = FXCollections.observableList(cellList);
        kubTable.setItems(podCellList);



    }
}
