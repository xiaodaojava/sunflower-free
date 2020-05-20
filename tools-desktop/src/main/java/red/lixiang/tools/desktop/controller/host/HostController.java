package red.lixiang.tools.desktop.controller.host;

import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.utils.AlertTools;
import red.lixiang.tools.jdk.RandomTools;
import red.lixiang.tools.jdk.StringTools;
import red.lixiang.tools.jdk.os.HostTools;
import red.lixiang.tools.jdk.os.OSTools;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/2/26
 **/
public class HostController implements Initializable{

    public ListView<HostCell> hostListField;

    public TextArea hostContentField;

    private String rootPassword = "";

    ObservableList<HostCell> observableHostList;
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
        List<HostConfig> hostConfigList  = GlobalConfig.getConfig().getHostConfigList();
        List<HostCell> hostCellList = hostConfigList.stream()
                .map((x) -> new HostCell(x.getTag(), x.getSelected(),x.getContent()))
                .collect(Collectors.toList());
        observableHostList = FXCollections.observableList(hostCellList);
        hostListField.setItems(observableHostList);

        for (HostCell cell : observableHostList) {
            initHostCell(cell);
        }
        // 点击的时候
        hostListField.getSelectionModel().selectedItemProperty().addListener(
                (ov, oldVal, newVal) -> {
                    for (HostCell config : observableHostList) {
                        if(config.label.getText().equals(newVal.label.getText())){
                            hostContentField.setUserData(config);
                            hostContentField.setText(config.content);
                        }
                    }
                });

        hostContentField.textProperty().addListener(((observable, oldValue, newValue) -> {
            HostCell cell = (HostCell)hostContentField.getUserData();
            if(cell==null){ return; }
            cell.content = newValue;
            convertToConfig();
        }));

    }

    public void initHostCell(HostCell cell ){
        // 点编辑的时候
        cell.edit.setOnAction(event -> {
            TextInputDialog dialog = new TextInputDialog(cell.label.getText());
            dialog.setContentText("新的标签名:");
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(name -> {cell.label.setText(name);convertToConfig();});
        });
        // 点删除的时候
        cell.remove.setOnAction(event -> {
            Alert alert = AlertTools.confirmAlert("是否确认删除这个host?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get()== ButtonType.OK) {
                // 点了删除的时候. 移除当前的cell
               observableHostList.remove(cell);
               //  保存host
               saveHostToLocal();

            }
        });
        //勾选的时候
        cell.checkBox.setOnAction(event -> {
            String s = saveHostToLocal();
            if(s.contains("fail")){
                AlertTools.popDialog(s);
                cell.checkBox.setSelected(!cell.checkBox.isSelected());
                convertToConfig();
            }
        });
    }



    public List<HostConfig> convertToConfig(){
        //转成我们通用的configList
        List<HostConfig> hostConfigList = observableHostList.stream().map(x -> {
            HostConfig config = new HostConfig();
            config.setSelected(x.checkBox.isSelected());
            config.setTag(x.label.getText());
            config.setContent(x.content);
            return config;
        }).collect(Collectors.toList());
        // 替换配置
        GlobalConfig.getConfig().setHostConfigList(hostConfigList);
        return hostConfigList;
    }

    public String saveHostToLocal(){
        List<HostConfig> hostConfigList = convertToConfig();
        String content = hostConfigList.stream().filter(HostConfig::getSelected).map(HostConfig::getContent).collect(Collectors.joining("\n"));
        String s = HostTools.saveHost(content);
        if(s.contains("fail")){
            if(OSTools.isMac() && StringTools.isBlank(rootPassword)){
                //是mac,并且rootPassword为空的时候
                TextInputDialog dialog = new TextInputDialog();
                dialog.setContentText("电脑管理员密码:");
                Optional<String> result = dialog.showAndWait();
                result.ifPresent(pass -> {rootPassword = pass ;convertToConfig();});
            }
            String execResult = HostTools.saveHostByRoot(content, rootPassword);
            System.out.println(execResult);
            s= "OK";
        }
        return s;

    }
    /**
     * 编辑配置
     * @param actionEvent
     */
    public void saveHostConfig(ActionEvent actionEvent) {
        //给list添加一个
        HostCell cell = new HostCell(RandomTools.getComCharStr(5),false,"# welcome to sunflower! ");
        initHostCell(cell);
        observableHostList.add(cell);
    }

    public void refreshHost(ActionEvent actionEvent) {
        saveHostToLocal();
    }
}
