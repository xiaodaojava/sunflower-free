package red.lixiang.tools.desktop.controller.ssh;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.controller.ssh.terminal.TerminalController;


import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/3/30
 **/
public class SSHController implements Initializable {

    public TableView<SSHCell> sshTable;

    GlobalConfig globalConfig = GlobalConfig.getConfig();

    ObservableList<SSHCell> sshCellList;


    public TableColumn<SSHCell,String> tagColumn;
    public TableColumn<SSHCell,String> ipColumn;
    public TableColumn<SSHCell,String> usernameColumn;
    public TableColumn optColumn;



    public void newSSHConfig(ActionEvent actionEvent) {
        Stage editStage = new Stage();
        editStage.setTitle("新增配置");
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.setMinWidth(600);
        editStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/ssh/new.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root,600,500);
        SSHEditController controller = fxmlLoader.getController();
        controller.init(x->{
            sshCellList.add(new SSHCell(x));
            saveLocal();
            editStage.close();
        });
        editStage.setScene(scene);
        editStage.showAndWait();

    }

    public void saveLocal(){
        List<SSHConfig> configList = sshCellList.stream().map(SSHCell::getConfig).collect(Collectors.toList());
        globalConfig.setSshConfigList(configList);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //为每个column绑定数据
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
        ipColumn.setCellValueFactory(new PropertyValueFactory<>("ip"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        optColumn.setCellFactory(param -> {
            TableCell<SSHCell,String> cell  = new TableCell<>(){
                final Button btn = new Button("连接");
                @Override
                protected void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            SSHCell sshCell = getTableView().getItems().get(getIndex());
//                            Stage conStage = new Stage();
//                            conStage.setTitle(sshCell.getTag()+":"+sshCell.getIp());
//                            conStage.initModality(Modality.WINDOW_MODAL);
//                            conStage.setMinWidth(800);
//                            conStage.setMinHeight(500);
//                            FXMLLoader fxmlLoader = new FXMLLoader();
//                            fxmlLoader.setLocation(getClass().getResource("/tools/ssh/connect.fxml"));
//                            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
//                            Parent root = null;
//                            try {
//                                root = fxmlLoader.load();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                            Scene scene = new Scene(root,800,500);
//                            SSHConnectController controller = fxmlLoader.getController();
//                            controller.initConn(sshCell.getConfig());
//                            controller.setStage(conStage);
//                            conStage.setOnHiding((e)->{controller.close();});
//                            conStage.setScene(scene);
//                            conStage.showAndWait();

                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("/tools/ssh/terminal.fxml"));
                            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                            Parent root = null;
                            try {
                                root = fxmlLoader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Scene scene = new Scene(root,800,500);
                            scene.getStylesheets().add(SSHController.class.getResource("/tools/ssh/Styles.css").toExternalForm());
                            Stage conStage = new Stage();
                            TerminalController controller = fxmlLoader.getController();
                            controller.initConn(sshCell.getConfig());
                            controller.setStage(conStage);
                            conStage.setScene(scene);
                            conStage.initModality(Modality.WINDOW_MODAL);
                            conStage.setMinWidth(800);
                            conStage.setMinHeight(500);
                            RunParam.RunParam().setCurrentStage(conStage);
                            conStage.showAndWait();
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        // 去读配置,进行初始化
        List<SSHConfig> sshConfigList = globalConfig.getSshConfigList();
        sshCellList = FXCollections.observableList(sshConfigList.stream().map(SSHCell::new).collect(Collectors.toList()));
        sshTable.setItems(sshCellList);
    }
}
