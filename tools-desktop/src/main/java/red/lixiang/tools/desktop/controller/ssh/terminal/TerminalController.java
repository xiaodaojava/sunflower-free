package red.lixiang.tools.desktop.controller.ssh.terminal;

import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.PTYMode;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import red.lixiang.tools.desktop.controller.ssh.SSHConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;

import static red.lixiang.tools.desktop.controller.ssh.SSHConfig.AUTH_PASSWORD;

public class TerminalController {

    public Stage stage;

    public SSHConfig sshConfig;

    public TabPane tabPane;

    /** shell的输出流 */
    OutputStream outputStream;

    SSHClient ssh;
    Session session;

    /**
     * 初始化连接
     * @param config
     */
    public void initConn(SSHConfig config){
        this.sshConfig = config;
        TerminalConfig defaultConfig = new TerminalConfig();

        TerminalBuilder terminalBuilder = new TerminalBuilder(defaultConfig);
        TerminalTab terminal = terminalBuilder.newTerminal(sshConfig);

        tabPane.getTabs().add(terminal);
       // doConn();
    }



    public TerminalController setStage(Stage stage) {
        this.stage = stage;
        return this;
    }
}
