package red.lixiang.tools.desktop.controller.ssh;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author lixiang
 * @date 2020/4/1
 **/
public class SSHCell {

    private SSHConfig config;
    private SimpleLongProperty id = new SimpleLongProperty();
    private SimpleStringProperty tag = new SimpleStringProperty();
    private SimpleStringProperty ip = new SimpleStringProperty();
    private SimpleStringProperty username = new SimpleStringProperty();


    public SSHCell(SSHConfig config) {
        this.config = config;
        setTag(config.getTag());
        setIp(config.getIp());
        setUsername(config.getUsername());

    }

    public SSHConfig getConfig() {
        return config;
    }

    public SSHCell setConfig(SSHConfig config) {
        this.config = config;
        return this;
    }

    public long getId() {
        return id.get();
    }

    public SimpleLongProperty idProperty() {
        return id;
    }

    public void setId(long id) {
        this.id.set(id);
    }

    public String getTag() {
        return tag.get();
    }

    public SimpleStringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public String getIp() {
        return ip.get();
    }

    public SimpleStringProperty ipProperty() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip.set(ip);
    }

    public String getUsername() {
        return username.get();
    }

    public SimpleStringProperty usernameProperty() {
        return username;
    }

    public void setUsername(String username) {
        this.username.set(username);
    }

}
