package red.lixiang.tools.desktop.controller.image;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import red.lixiang.tools.common.github.GitConfig;

import java.util.Optional;

/**
 * @author lixiang
 * @date 2020/2/27
 **/
public class BedCell extends HBox {
    Long id;
    RadioButton check = new RadioButton();
    Label label = new Label();
//    Button edit = new Button("e");
//    Button remove = new Button("r");

    GitConfig config;


    /**
     * Creates an HBox layout with spacing = 0.
     */
    public BedCell(String tag, GitConfig config) {
        super();
        this.config = config;
        check.setSelected(config.getSelected()!=null && config.getSelected());
        label.setText(tag);
        label.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(label, Priority.ALWAYS);
        this.getChildren().addAll(check,label);

    }
}
