package red.lixiang.tools.desktop;

/**
 * @author lixiang
 * @date 2020/2/20
 **/

import com.alibaba.fastjson.JSON;
import red.lixiang.tools.common.kubernetes.KubernetesConfig;
import red.lixiang.tools.desktop.bean.BeanMap;
import red.lixiang.tools.desktop.conf.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import red.lixiang.tools.desktop.controller.host.HostConfig;
import red.lixiang.tools.desktop.controller.ssh.SSHConfig;
import red.lixiang.tools.desktop.manager.set.SetManager;
import red.lixiang.tools.desktop.utils.SunflowerTray;
import red.lixiang.tools.jdk.file.FileTools;
import red.lixiang.tools.jdk.ListTools;
import red.lixiang.tools.jdk.StringTools;
import red.lixiang.tools.jdk.ToolsLogger;
import red.lixiang.tools.jdk.os.HostTools;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static red.lixiang.tools.desktop.conf.RunParam.*;

public class DesktopMain extends Application {


    private String configFile = ConfigDir + "sunflower.conf";

    @Override
    public void start(Stage stage){
        try{
            // 加载配置
            RunParam runParam = RunParam.RunParam();
            loadConfig();

            //加载资源文件,用于定义界面
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/tools/main.fxml"));
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 1000, 600);
            stage.setScene(scene);
            runParam.setMainStage(stage);
            stage.setOnHiding(event -> {
                CloseParam.close();
            });
            stage.setOnCloseRequest(e -> {
                SunflowerTray.getInstance().hide(stage);
                SunflowerTray.getInstance().listen(stage);
            });
            stage.show();

        }catch (Exception e){
            ToolsLogger.error("启动出错",e);
        }

    }


    public void loadConfig() {
        try {

            //去加载配置,如果配置路径不存在，则新建
            if (Files.notExists(Paths.get(ConfigDir))) {
                Files.createDirectory(Paths.get(ConfigDir));
            }
            // 创建日志目录，如果配置路径不存在，则新建
            if (Files.notExists(Paths.get(LogDir))) {
                Files.createDirectory(Paths.get(LogDir));
            }
            String logFilePath = LogDir + ".sunflower.log";
            Files.deleteIfExists(Paths.get(logFilePath));
            ToolsLogger.init(logFilePath);
            //对k8s,host单独创建配置文件夹
            if (Files.notExists(Paths.get(KubeConfigDir))) {
                Files.createDirectory(Paths.get(KubeConfigDir));
            }
            if (Files.notExists(Paths.get(HostConfigDir))) {
                Files.createDirectory(Paths.get(HostConfigDir));
            }
            if (Files.notExists(Paths.get(SSHConfigDir))) {
                Files.createDirectory(Paths.get(SSHConfigDir));
            }
            //读取ssh/k8s/host配置
            List<SSHConfig> sshConfigList = Files.list(Paths.get(SSHConfigDir)).map(x -> {
                try {
                    byte[] bytes = Files.readAllBytes(x);
                    ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
                    return (SSHConfig) inputStream.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            List<KubernetesConfig> kubernetesConfigList = Files.list(Paths.get(KubeConfigDir))
                    .filter(x -> {
                        String s = x.toString();
                        String fileName = FileTools.getFileNameFromPath(s);
                        return !fileName.contains(".");
                    }).map(x -> {
                        try {
                            String content = Files.readString(x, StandardCharsets.UTF_8);
                            KubernetesConfig config = new KubernetesConfig();
                            config.loadConfigFromStr(content);
                            config.setTag(x.getFileName().toString());
                            return config;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }).collect(Collectors.toList());

            List<HostConfig> hostConfigs = Files.list(Paths.get(HostConfigDir)).map(x -> {
                try {
                    String content = Files.readString(x, StandardCharsets.UTF_8);
                    HostConfig config = new HostConfig();
                    config.setContent(content);
                    boolean selected = x.getFileName().toString().startsWith("1");
                    config.setSelected(selected);
                    config.setTag(x.getFileName().toString().substring(1));
                    return config;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }).collect(Collectors.toList());
            if (ListTools.isBlank(hostConfigs)) {
                //没有这个工具留下的host记录, 去读当前的Host,然后添加一个current
                String s = HostTools.loadHost();
                HostConfig current = new HostConfig();
                current.setContent(s).setTag("current").setSelected(true);
                hostConfigs = Collections.singletonList(current);
            }
            if(Files.notExists(Paths.get(configFile))){
                //如果配置文件不存在的话,去新建一个
                Files.writeString(Paths.get(configFile),"");
            }
            String s = Files.readString(Paths.get(configFile),StandardCharsets.UTF_8);
            GlobalConfig outConfig = JSON.parseObject(s, GlobalConfig.class);
            GlobalConfig config = GlobalConfig.getConfig();
            config.copy(outConfig);
            config.setKubConfigList(kubernetesConfigList);
            config.setHostConfigList(hostConfigs);
            config.setSshConfigList(sshConfigList);
        } catch (Exception e) {
            ToolsLogger.error("加载配置出错",e);
            e.printStackTrace();
        }
        //初始化beanMap
        BeanMap.initBean();
    }

    /**
     * This method is called when the application should stop, and provides a
     * convenient place to prepare for application exit and destroy resources.
     *
     * <p>
     * The implementation of this method provided by the Application class does nothing.
     * </p>
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @throws Exception if something goes wrong
     */
    @Override
    public void stop() throws Exception {

        //这时候去保存配置
        GlobalConfig config = GlobalConfig.getConfig();
        String s = JSON.toJSONString(config);
        Files.writeString(Paths.get(configFile), s);

        System.out.println("Welcome Back");
    }


    public static void main(String[] args) {
        launch();
    }

}