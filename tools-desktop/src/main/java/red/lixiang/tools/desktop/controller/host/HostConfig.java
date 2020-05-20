package red.lixiang.tools.desktop.controller.host;


/**
 * @author lixiang
 * @date 2020/2/26
 **/
public class HostConfig {

    /**
     * 当前配置的标识
     */
    private Long id;

    /**
     * 这份host的标签
     */
    private String tag;

    /**
     * 是否是当前生效的host
     */
    private Boolean selected;

    /**
     * 这份host的内容
     */
    private String content;

    public Long getId() {
        return id;
    }

    public HostConfig setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public HostConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getContent() {
        return content;
    }

    public HostConfig setContent(String content) {
        this.content = content;
        return this;
    }

    public Boolean getSelected() {
        return selected;
    }

    public HostConfig setSelected(Boolean selected) {
        this.selected = selected;
        return this;
    }

}
