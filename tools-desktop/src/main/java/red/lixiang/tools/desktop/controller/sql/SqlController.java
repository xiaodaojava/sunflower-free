package red.lixiang.tools.desktop.controller.sql;

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
import red.lixiang.tools.desktop.controller.ssh.SSHCell;
import red.lixiang.tools.desktop.controller.ssh.SSHConnectController;
import red.lixiang.tools.desktop.controller.ssh.SSHEditController;
import red.lixiang.tools.jdk.sql.SqlConfig;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/4/7
 **/
public class SqlController  implements Initializable {
    public TableView<SqlCell> sqlTable;
    public TableColumn<SqlCell,String> tagCol;
    public TableColumn<SqlCell,String> urlCol;
    public TableColumn<SqlCell,String> usernameCol;
    public TableColumn optCol;

    GlobalConfig globalConfig = GlobalConfig.getConfig();

    ObservableList<SqlCell> sqlCellList;


    public void saveToLocal(){
        List<SqlConfig> collect = sqlCellList.stream().map(SqlCell::getSqlConfig).collect(Collectors.toList());
        globalConfig.setSqlConfigList(collect);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 为每个column绑定数据
        tagCol.setCellValueFactory(new PropertyValueFactory<>("tag"));
        urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        optCol.setCellFactory(param -> {
            TableCell<SqlCell,String> cell  = new TableCell<>(){
                final Button btn = new Button("连接");
                @Override
                protected void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            SqlCell sqlCell = getTableView().getItems().get(getIndex());
                            Stage conStage = new Stage();
                            conStage.setTitle(sqlCell.getTag()+":"+sqlCell.getUrl());
                            conStage.initModality(Modality.WINDOW_MODAL);
                            conStage.setMinWidth(800);
                            conStage.setMinHeight(500);
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("/tools/sql/connect.fxml"));
                            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                            Parent root = null;
                            try {
                                root = fxmlLoader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Scene scene = new Scene(root,800,500);
                            SqlConController controller = fxmlLoader.getController();
                            controller.initConn(sqlCell.getSqlConfig());
                            controller.setStage(conStage);
                            conStage.setOnHiding((e)->{controller.close();});
                            conStage.setScene(scene);
                            conStage.showAndWait();
                        });
                        setGraphic(btn);
                        setText(null);
                    }
                }
            };
            return cell;
        });
        // 从config中获取初始化数据
        List<SqlConfig> sqlConfigList = globalConfig.getSqlConfigList();
        sqlCellList = FXCollections.observableList(sqlConfigList.stream().map(SqlCell::new).collect(Collectors.toList()));
        sqlTable.setItems(sqlCellList);
    }

    public void newConfig(ActionEvent actionEvent) {
        Stage editStage = new Stage();
        editStage.setTitle("新增配置");
        editStage.initModality(Modality.WINDOW_MODAL);
        editStage.setMinWidth(600);
        editStage.setMinHeight(500);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/tools/sql/new.fxml"));
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root,600,500);
        SqlEditController controller = fxmlLoader.getController();
        controller.init(x->{
            sqlCellList.add(new SqlCell(x));
            saveToLocal();
            editStage.close();
        });
        editStage.setScene(scene);
        editStage.showAndWait();
    }
}
