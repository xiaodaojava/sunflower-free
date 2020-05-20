package red.lixiang.tools.desktop.controller.common;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * @author lixiang
 * @date 2020/3/28
 **/
public class TranslatorController {
    public TextArea origin;
    public WebView webView;

    public void translator(ActionEvent actionEvent) {

        String text = origin.getText();
        WebEngine engine = webView.getEngine();
        engine.load("https://fanyi.baidu.com/#zh/en/"+text);

    }
}
