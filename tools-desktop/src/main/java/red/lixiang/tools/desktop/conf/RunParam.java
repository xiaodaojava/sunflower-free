package red.lixiang.tools.desktop.conf;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import red.lixiang.tools.jdk.os.OSTools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
public class RunParam {

    private static RunParam runParam = new RunParam();

    public static  RunParam RunParam(){
        return runParam;
    }
    private RunParam(){

    }

    //*********运行时需要的数据

    public static String ConfigDir = OSTools.userHomePath()+"/.sunflower/";

    public static String KubeConfigDir = OSTools.userHomePath()+"/.sunflower/kube/";
    public static String HostConfigDir = OSTools.userHomePath()+"/.sunflower/host/";
    public static String SSHConfigDir = OSTools.userHomePath()+"/.sunflower/ssh/";
    public static String LogDir = OSTools.userHomePath()+"/.sunflower/logs/";


    private Stage mainStage;

    private Stage kubEditStage;

    private Stage loginStage;

    /** 代表当前的stage */
    private Stage currentStage;

    private Pane kubPane;

    //********


    public Stage getCurrentStage() {
        return currentStage;
    }

    public RunParam setCurrentStage(Stage currentStage) {
        this.currentStage = currentStage;
        return this;
    }

    public Stage getLoginStage() {
        return loginStage;
    }

    public RunParam setLoginStage(Stage loginStage) {
        this.loginStage = loginStage;
        return this;
    }

    public Pane getKubPane() {
        return kubPane;
    }

    public RunParam setKubPane(Pane kubPane) {
        this.kubPane = kubPane;
        return this;
    }

    public Stage getKubEditStage() {
        return kubEditStage;
    }

    public RunParam setKubEditStage(Stage kubEditStage) {
        this.kubEditStage = kubEditStage;
        return this;
    }

    public Stage getMainStage() {
        return mainStage;
    }

    public RunParam setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
        return this;
    }

    public static void main(String[] args) throws IOException {
        String[] cmd = {"/bin/bash","-c","echo xxdxxdxxd | sudo -S echo bbb>a.txt"};
        Process pb = Runtime.getRuntime().exec(cmd);

        String line;
        BufferedReader input = new BufferedReader(new InputStreamReader(pb.getInputStream()));
        while ((line = input.readLine()) != null) {
            System.out.println(line);
        }
        input.close();
    }
}
