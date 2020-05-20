package red.lixiang.tools.desktop.controller.kub;

import javafx.beans.property.SimpleStringProperty;
import red.lixiang.tools.jdk.StringTools;

/**
 * @author lixiang
 * @date 2020/3/22
 **/
public class KubPodCell {

    private SimpleStringProperty name = new SimpleStringProperty();

    private SimpleStringProperty namespace = new SimpleStringProperty();


    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getNamespace() {
        return namespace.get();
    }

    public SimpleStringProperty namespaceProperty() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace.set(namespace);
    }
}
