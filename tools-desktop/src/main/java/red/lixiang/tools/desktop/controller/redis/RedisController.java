package red.lixiang.tools.desktop.controller.redis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import red.lixiang.tools.common.redis.RedisCommonTools;
import red.lixiang.tools.common.redis.RedisConfig;
import red.lixiang.tools.common.redis.RedisValue;
import red.lixiang.tools.desktop.conf.CloseParam;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.controller.sql.SqlCell;
import red.lixiang.tools.desktop.controller.sql.SqlConController;
import red.lixiang.tools.desktop.utils.AlertTools;
import red.lixiang.tools.desktop.utils.FXMLTools;
import red.lixiang.tools.jdk.StringTools;
import red.lixiang.tools.jdk.sql.SqlConfig;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/3/11
 **/
public class RedisController implements Initializable {


    public GlobalConfig globalConfig = GlobalConfig.getConfig();

    public TableView<RedisTableCell> redisTable;
    public TableColumn<RedisTableCell,String> tagColumn;
    public TableColumn<RedisTableCell,String> hostColumn;
    public TableColumn optColumn;

    ObservableList<RedisTableCell> redisCellList;



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
       // 初始化表格,为每个column绑定数据
        tagColumn.setCellValueFactory(new PropertyValueFactory<>("tag"));
        hostColumn.setCellValueFactory(new PropertyValueFactory<>("host"));
        optColumn.setCellFactory(param -> {
            TableCell<RedisTableCell,String> cell  = new TableCell<>(){
                final Button btn = new Button("连接");
                @Override
                protected void updateItem(String item, boolean empty) {
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        btn.setOnAction(event -> {
                            RedisTableCell redisCell = getTableView().getItems().get(getIndex());
                            Stage conStage = new Stage();
                            conStage.setTitle(redisCell.getTag()+":"+redisCell.getHost());
                            conStage.initModality(Modality.WINDOW_MODAL);
                            conStage.setMinWidth(800);
                            conStage.setMinHeight(500);
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(getClass().getResource("/tools/redis/con.fxml"));
                            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                            Parent root = null;
                            try {
                                root = fxmlLoader.load();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            Scene scene = new Scene(root,800,500);
                            ConController controller = fxmlLoader.getController();
                            controller.init(redisCell.getConfig(),conStage);
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
        // 读取配置,绑定数据
        List<RedisConfig> redisConfigList = globalConfig.getRedisConfigList();
        redisCellList = FXCollections.observableList(redisConfigList.stream().map(RedisTableCell::new).collect(Collectors.toList()));
        redisTable.setItems(redisCellList);

    }


    public void newConfig(ActionEvent actionEvent) {
        // 弹出新增的页面
        FXMLLoader loader = FXMLTools.initLoader("/tools/redis/con.fxml");
        Stage stage = FXMLTools.initStage(loader.getRoot(), 500d, 800d);
        NewController controller = loader.getController();
        controller.init(config -> {
            RedisTableCell cell = new RedisTableCell(config);
            redisCellList.add(cell);
            saveToLocal();
        },stage);

    }

    private void saveToLocal() {
        List<RedisConfig> redisConfigs = redisCellList.stream().map(RedisTableCell::getConfig).collect(Collectors.toList());
        globalConfig.setRedisConfigList(redisConfigs);
    }
}
