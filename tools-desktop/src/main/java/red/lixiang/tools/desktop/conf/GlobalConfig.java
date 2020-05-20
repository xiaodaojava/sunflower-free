package red.lixiang.tools.desktop.conf;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import red.lixiang.tools.common.github.GitConfig;
import red.lixiang.tools.common.kubernetes.KubernetesConfig;
import red.lixiang.tools.common.redis.RedisConfig;
import red.lixiang.tools.desktop.controller.host.HostConfig;
import red.lixiang.tools.desktop.controller.ssh.SSHConfig;
import red.lixiang.tools.jdk.FileTools;
import red.lixiang.tools.jdk.sql.SqlConfig;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static red.lixiang.tools.desktop.conf.RunParam.*;

/**
 * 每新增加一项配置 1.get/set方法, 2.修改copy方法
 *
 * @author lixiang
 * @date 2020/2/23
 **/
public class GlobalConfig {

    private static final GlobalConfig config = new GlobalConfig();

    public static GlobalConfig getConfig() {
        return config;
    }


    private GlobalConfig() {

    }

    private String configFile = ConfigDir + "sunflower.conf";

    private Long passportId;
    private String token;
    /**
     * 是否登录
     */
    private boolean loginFlag = false;
    /**
     * 是否订阅
     */
    private boolean subscribeFlag = false;
    private String currentVersion = "1.0.4";
    private String softTitle;
    private String globalNotice;

    @JSONField(serialize = false)
    private List<KubernetesConfig> kubConfigList = new ArrayList<>();

    @JSONField(serialize = false)
    private List<HostConfig> hostConfigList = new ArrayList<>();

    @JSONField(serialize = false)
    private List<SSHConfig> sshConfigList = new ArrayList<>();

    private List<SqlConfig> sqlConfigList = new ArrayList<>();

    private List<RedisConfig> redisConfigList = new ArrayList<>();

    private List<GitConfig> gitConfigList = new ArrayList<>();

    public void copy(GlobalConfig outConfig) {
        if (outConfig == null) {
            return;
        }
        this.token = outConfig.getToken();
        this.passportId = outConfig.getPassportId();
        this.currentVersion = outConfig.getCurrentVersion();
        this.softTitle = outConfig.getSoftTitle();
        this.redisConfigList = outConfig.getRedisConfigList();
        this.gitConfigList = outConfig.getGitConfigList();
        this.sqlConfigList = outConfig.getSqlConfigList();
    }


    public void saveToLocal() {

        try {
            //k8s和host的要单独保存
            FileTools.deleteDirFiles(KubeConfigDir);
            FileTools.deleteDirFiles(HostConfigDir);
            for (KubernetesConfig config : kubConfigList) {
                Files.write(Paths.get(KubeConfigDir + config.getTag()),config.getRawContent().getBytes());
            }
            for (HostConfig config : hostConfigList) {
                String fileName  =HostConfigDir+(config.getSelected()?"1":"0")+config.getTag() ;
                Files.write(Paths.get(fileName),config.getContent().getBytes());
            }
            for (SSHConfig config : sshConfigList) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
                outputStream.writeObject(config);
                byte[] bytes = byteArrayOutputStream.toByteArray();
                Files.write(Paths.get(SSHConfigDir + config.getTag()),bytes);
            }

            String s = JSON.toJSONString(config);
            Files.write(Paths.get(configFile), s.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<SqlConfig> getSqlConfigList() {
        return sqlConfigList;
    }

    public GlobalConfig setSqlConfigList(List<SqlConfig> sqlConfigList) {
        this.sqlConfigList = sqlConfigList;
        return this;
    }

    public boolean isLoginFlag() {
        return loginFlag;
    }

    public GlobalConfig setLoginFlag(boolean loginFlag) {
        this.loginFlag = loginFlag;
        return this;
    }

    public boolean isSubscribeFlag() {
        return subscribeFlag;
    }

    public GlobalConfig setSubscribeFlag(boolean subscribeFlag) {
        this.subscribeFlag = subscribeFlag;
        return this;
    }

    public String getToken() {
        return token;
    }

    public GlobalConfig setToken(String token) {
        this.token = token;
        return this;
    }

    public List<GitConfig> getGitConfigList() {
        return gitConfigList;
    }

    public GlobalConfig setGitConfigList(List<GitConfig> gitConfigList) {
        this.gitConfigList = gitConfigList;
        return this;
    }

    public List<RedisConfig> getRedisConfigList() {
        return redisConfigList;
    }

    public GlobalConfig setRedisConfigList(List<RedisConfig> redisConfigList) {
        this.redisConfigList = redisConfigList;
        return this;
    }

    public List<SSHConfig> getSshConfigList() {
        return sshConfigList;
    }

    public GlobalConfig setSshConfigList(List<SSHConfig> sshConfigList) {
        this.sshConfigList = sshConfigList;
        return this;
    }

    public List<HostConfig> getHostConfigList() {
        return hostConfigList;
    }

    public GlobalConfig setHostConfigList(List<HostConfig> hostConfigList) {
        this.hostConfigList = hostConfigList;
        return this;
    }

    public Long getPassportId() {
        return passportId;
    }

    public GlobalConfig setPassportId(Long passportId) {
        this.passportId = passportId;
        return this;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public GlobalConfig setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
        return this;
    }

    public String getSoftTitle() {
        return softTitle;
    }

    public GlobalConfig setSoftTitle(String softTitle) {
        this.softTitle = softTitle;
        return this;
    }

    public String getGlobalNotice() {
        return globalNotice;
    }

    public GlobalConfig setGlobalNotice(String globalNotice) {
        this.globalNotice = globalNotice;
        return this;
    }

    public List<KubernetesConfig> getKubConfigList() {
        return kubConfigList;
    }

    public GlobalConfig setKubConfigList(List<KubernetesConfig> kubConfigList) {
        this.kubConfigList = kubConfigList;
        return this;
    }
}
