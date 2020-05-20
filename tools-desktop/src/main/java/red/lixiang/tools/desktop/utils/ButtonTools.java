package red.lixiang.tools.desktop.utils;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * @author lixiang
 * @date 2020/5/5
 **/
public class ButtonTools {
    public static boolean doubleClick(MouseEvent event){
        return event.getButton()== MouseButton.PRIMARY && event.getClickCount()==2;
    }
}
