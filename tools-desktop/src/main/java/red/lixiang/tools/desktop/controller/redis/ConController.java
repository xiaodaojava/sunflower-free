package red.lixiang.tools.desktop.controller.redis;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import red.lixiang.tools.common.redis.RedisCommonTools;
import red.lixiang.tools.common.redis.RedisConfig;
import red.lixiang.tools.common.redis.RedisValue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * 连接的controller
 * @author lixiang
 * @date 2020/5/7
 **/
public class ConController  implements Initializable {

    public ListView<String> keyListView;
    public TextField searchField;
    public TextArea valueArea;

    Stage stage;

    RedisConfig curConfig;

    RedisCommonTools tools;

    ObservableList<String> redisKeyList = FXCollections.observableList(new ArrayList<>());

    public void init(RedisConfig config , Stage stage){
        this.curConfig  = config;
        this.stage = stage;
        tools=new RedisCommonTools(curConfig);
        searchKey(null);
    }

    public void close(){
        tools.close();
    }


    public void search(String key, boolean init) {
        List<String> keys = tools.scan(init, key, 50L);
        if (init) {
            redisKeyList.clear();
        }
        redisKeyList.addAll(keys);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        valueArea.setWrapText(true);

        // 给ListView绑定事件
        keyListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, oldVal, newVal) -> {
                    RedisValue redisValue = tools.get(newVal);
                    valueArea.setText(redisValue.getValue());
                });

        keyListView.setCellFactory(lv -> {

            ListCell<String> cell = new ListCell<>();

            ContextMenu contextMenu = new ContextMenu();

            MenuItem deleteItem = new MenuItem("delete");
            deleteItem.setOnAction(event ->{
                String selectedItem = lv.getSelectionModel().getSelectedItem();
                keyListView.getItems().remove(selectedItem);
                tools.del(selectedItem);
            } );
            contextMenu.getItems().addAll(deleteItem);
//            cell.setContextMenu(contextMenu);
            cell.textProperty().bind(cell.itemProperty());
//
            cell.emptyProperty().addListener((obs, wasEmpty, isNowEmpty) -> {
                if (isNowEmpty) {
                    cell.setContextMenu(null);
                } else {
                    cell.setContextMenu(contextMenu);
                }
            });
            return cell ;
        });
        keyListView.setItems(redisKeyList);
    }

    public void searchKey(ActionEvent actionEvent) {
        String key = searchField.getText() == null ? "*" : searchField.getText();
        search(key, true);
    }

    public void moreSearch(ActionEvent actionEvent) {
        String key = searchField.getText() == null ? "*" : searchField.getText();
        search(key, false);
    }
}
