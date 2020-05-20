package red.lixiang.tools.desktop.controller.ssh;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static red.lixiang.tools.desktop.conf.RunParam.SSHConfigDir;

/**
 * @author lixiang
 * @date 2020/4/1
 **/
public class SSHConfig implements Serializable {

    /** 用户名密码的形式登录 */
    public static final int AUTH_PASSWORD=1;
    /** private key 的形式登录 */
    public static final int AUTH_PRI_KEY = 2;

    private Long id ;
    private String tag ;
    private String ip ;
    private String username ;
    private String privateKey ;
    private String password;
    /** 验证类型 */
    private Integer authType;

    public Integer getAuthType() {
        return authType;
    }

    public SSHConfig setAuthType(Integer authType) {
        this.authType = authType;
        return this;
    }

    public Long getId() {
        return id;
    }

    public SSHConfig setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public SSHConfig setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public SSHConfig setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public SSHConfig setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public SSHConfig setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public SSHConfig setPassword(String password) {
        this.password = password;
        return this;
    }

    public static void main(String[] args) {

    }
}
