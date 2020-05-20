package red.lixiang.tools.desktop.controller.sql;

import javafx.beans.property.SimpleStringProperty;
import red.lixiang.tools.jdk.sql.SqlConfig;

/**
 * @author lixiang
 * @date 2020/4/7
 **/
public class SqlCell {

    private  SqlConfig sqlConfig;
    private SimpleStringProperty tag = new SimpleStringProperty();
    private SimpleStringProperty url = new SimpleStringProperty();
    private SimpleStringProperty username = new SimpleStringProperty();

    public SqlCell(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
        setTag(sqlConfig.getTag());
        setUrl(sqlConfig.getUrl());
        setUsername(sqlConfig.getUsername());
    }

    public SqlConfig getSqlConfig() {
        return sqlConfig;
    }

    public SqlCell setSqlConfig(SqlConfig sqlConfig) {
        this.sqlConfig = sqlConfig;
        return this;
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

    public String getUrl() {
        return url.get();
    }

    public SimpleStringProperty urlProperty() {
        return url;
    }

    public void setUrl(String url) {
        this.url.set(url);
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
