package red.lixiang.tools.desktop.controller.common;

import javafx.event.ActionEvent;
import javafx.scene.control.TextArea;
import red.lixiang.tools.jdk.JSONTools;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * @author lixiang
 * @date 2020/3/28
 **/
public class JsonFormatterController {

    public TextArea originText;

    public TextArea formatText;

    public void formatJson(ActionEvent actionEvent) {
        //先获取原来的值
        String origin = originText.getText();
        // 工具类转换
        String format = JSONTools.formatJson(origin);
        // 转换之后的值
        formatText.setText(format);
    }

    public void base64Encode(ActionEvent actionEvent) {
        //先获取原来的值
        String origin = originText.getText();
        // 工具类转换
        String format = Base64.getEncoder().encodeToString(origin.getBytes(StandardCharsets.UTF_8));
        // 转换之后的值
        formatText.setText(format);
    }

    public void base64Decode(ActionEvent actionEvent) {
        //先获取原来的值
        String origin = originText.getText();
        // 工具类转换
        byte[] decode = Base64.getDecoder().decode(origin.getBytes(StandardCharsets.UTF_8));
        String format = new String(decode);
        // 转换之后的值
        formatText.setText(format);
    }

    public void urlEncode(ActionEvent actionEvent) {
        //先获取原来的值
        String origin = originText.getText();
        // 工具类转换
        String encode = URLEncoder.encode(origin, StandardCharsets.UTF_8);
        // 转换之后的值
        formatText.setText(encode);
    }

    public void urlDecode(ActionEvent actionEvent) {
        //先获取原来的值
        String origin = originText.getText();
        // 工具类转换
        String encode = URLDecoder.decode(origin, StandardCharsets.UTF_8);
        // 转换之后的值
        formatText.setText(encode);
    }
}
