package red.lixiang.tools.desktop.controller.ssh;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.common.LoggerFactory;
import net.schmizz.sshj.common.StreamCopier;
import net.schmizz.sshj.connection.ConnectionException;
import net.schmizz.sshj.connection.channel.direct.PTYMode;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.TransportException;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import net.schmizz.sshj.userauth.UserAuthException;
import net.schmizz.sshj.xfer.FileSystemFile;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.jdk.StringTools;
import red.lixiang.tools.jdk.ToolsLogger;
import red.lixiang.tools.jdk.io.IOTools;

import java.io.*;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import static red.lixiang.tools.desktop.controller.ssh.SSHConfig.AUTH_PASSWORD;

/**
 * @author lixiang
 * @date 2020/3/30
 **/
public class SSHConnectController {

    public Stage stage;

    public SSHConfig sshConfig;

    public TextArea terminalArea;

    public TextField fileName;

    /** 命令输入框 */
    public TextField commandField;

    /** 当前远程机器的目录 */
    private volatile String curDir;

    /** 是否是当前目录的命令 */
    private volatile Boolean curComFlag =false;

    /** shell的输出流 */
    OutputStream outputStream;

    SSHClient ssh;
    Session session;


    public void sendCommand(ActionEvent actionEvent) {
        //对event的处理
        //发送命令
        doSendCommand(null);
    }

    public void doSendCommand(String cusCommand){

        String command = (StringTools.isNotBlank(cusCommand)?cusCommand:commandField.getText())+"\n";
        curComFlag = "pwd\n".equals(command);
        if(curComFlag) {
            curDir="";
        }
        commandField.clear();
        try {
            outputStream.write(command.getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public  void readString(InputStream from) throws IOException {
        Runnable runnable = ()->{
            try{
                byte[] buffer = new byte[1];
                while( from.read(buffer) != -1) {
                    String s = new String(buffer);
                    if(curComFlag){
                        curDir+=s;
                    }
                    Platform.runLater(()->{
                        terminalArea.appendText(s);
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();

    }

    /**
     * 初始化连接
     * @param config
     */
    public void initConn(SSHConfig config){
        this.sshConfig = config;
        doConn();
    }

    /**
     * 用当前的config进行连接
     */
    public void doConn(){
        //开始连接
        try{
            ssh = new SSHClient();
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(sshConfig.getIp());
            if(AUTH_PASSWORD==sshConfig.getAuthType()){

                ssh.authPassword(sshConfig.getUsername(),sshConfig.getPassword());
            }
            session = ssh.startSession();
            try {
                session.allocatePTY("vt102", 80, 24, 0, 0, Collections.<PTYMode, Integer>emptyMap());
                Session.Shell shell = session.startShell();
                InputStream inputStream = shell.getInputStream();
                InputStream errorStream = shell.getErrorStream();
                readString(inputStream);
                readString(errorStream);
                outputStream = shell.getOutputStream();

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                //  session.close();
                //  ssh.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 重新连接
     * @param actionEvent
     */
    public void reConnect(ActionEvent actionEvent) {
        // 先关闭
        close();
        // 重新连接
        doConn();
    }

    /**
     * 关闭session , 关闭sshClient
     */
    public void close(){
        ToolsLogger.info("closing session and client");
        try {
            if(session!=null){
                session.close();
            }
            if(ssh!=null){
                ssh.disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 监听命令框的回车事件
     * @param keyEvent
     */
    public void keyReleased(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        if(KeyCode.ENTER != keyCode){
            return;
        }
        // 可以肯定,就是回车了
        doSendCommand(null);
    }

    /**
     *  选择文件上传
     * @param actionEvent
     */
    public void uploadFile(ActionEvent actionEvent) {
        doSendCommand("pwd");
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择要上传的文件");
        File file = chooser.showOpenDialog(stage);
        var filePath = file.getAbsolutePath();
        try {
            String target = curDir.split("\r\n")[1]+"/";
            ssh.newSCPFileTransfer().upload(new FileSystemFile(filePath), target);
        } catch (IOException  e) {
            e.printStackTrace();
        }
    }

    public SSHConnectController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }

    public void downloadFile(ActionEvent actionEvent) {
        try{
            doSendCommand("pwd");
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("选择要下载文件的位置");
            File file = chooser.showDialog(stage);
            var filePath = file.getAbsolutePath()+"/";
            ToolsLogger.info(filePath);
            String targetFile;
            if((targetFile=fileName.getText())==null){
                return;
            }
            String target = curDir.split("\r\n")[1]+"/"+targetFile;
            ssh.newSCPFileTransfer().download(target, new FileSystemFile(filePath));
        } catch (  IOException e) {
            e.printStackTrace();
        }

    }
}
