package red.lixiang.tools.desktop.controller.common;

import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author lixiang
 * @date 2020/3/29
 **/
public class WebviewController  {

    public WebView webView;

    public TextField urlField;

    public void init(String url){
        urlField.setText(url);
        webView.getEngine().load(url);
    }



    public void keyReleased(KeyEvent keyEvent) {
        if(keyEvent.getCode()== KeyCode.ENTER){
            //如果是回车键的话
            String url = urlField.getText();
            webView.getEngine().load(url);
        }
    }
}
