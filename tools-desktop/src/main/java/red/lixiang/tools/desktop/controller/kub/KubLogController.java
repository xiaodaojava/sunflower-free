package red.lixiang.tools.desktop.controller.kub;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import red.lixiang.tools.common.kubernetes.KuberTools;
import red.lixiang.tools.common.kubernetes.KubernetesConfig;
import red.lixiang.tools.common.kubernetes.models.Pod;
import red.lixiang.tools.jdk.StringTools;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
public class KubLogController {

    public TextArea logContentArea;
    public TextField lineCount;

    private Pod currentPod;
    private KubernetesConfig  config;

    public void showLog(){
        String text = lineCount.getText();
        if(StringTools.isBlank(text)){
            text = "100";
        }
        String logContent = KuberTools.getPodLogs(Integer.valueOf(text), currentPod,null, config);
        logContent = logContent+"..获取时间:"+ LocalDateTime.now();
        // 获取焦点
        logContentArea.requestFocus();
        //设置文本

        logContentArea.setText(logContent);
        // 自动换行
        logContentArea.setWrapText(true);
        // 光标移到最下面
        logContentArea.appendText("");


    }

    public void refreshLog(ActionEvent actionEvent) {
        logContentArea.setText("");
        showLog();
    }

    public void init(Pod pod, KubernetesConfig config){
        this.currentPod = pod;
        this.config = config;
        showLog();
    }

}
