package red.lixiang.tools.desktop.controller.host;

import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 * @author lixiang
 * @date 2020/2/27
 **/
public class HostCell extends HBox {
    Label label = new Label();
    Button edit = new Button("e");
    Button remove = new Button("r");
    CheckBox checkBox = new CheckBox();
    String content;


    /**
     * Creates an HBox layout with spacing = 0.
     */
    public HostCell(String tag,boolean selected,String content) {
        super();
        label.setText(tag);
        this.content  = content;
        checkBox.setSelected(selected);
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);
        this.getChildren().addAll(checkBox,label,edit,remove);
    }
}
