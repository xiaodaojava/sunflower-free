package red.lixiang.tools.desktop.controller.redis;

import javafx.beans.property.SimpleStringProperty;
import red.lixiang.tools.common.redis.RedisConfig;

/**
 * @author lixiang
 * @date 2020/5/8
 **/
public class RedisTableCell {
    private RedisConfig config;
    private SimpleStringProperty tag = new SimpleStringProperty();
    private SimpleStringProperty host = new SimpleStringProperty();

    public RedisTableCell(RedisConfig config) {
        this.config = config;
        setHost(config.getHost());
        setTag(config.getTag());
    }

    public String getTag() {
        return tag.get();
    }

    public RedisConfig getConfig() {
        return config;
    }

    public RedisTableCell setConfig(RedisConfig config) {
        this.config = config;
        return this;
    }

    public SimpleStringProperty tagProperty() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag.set(tag);
    }

    public String getHost() {
        return host.get();
    }

    public SimpleStringProperty hostProperty() {
        return host;
    }

    public void setHost(String host) {
        this.host.set(host);
    }
}
