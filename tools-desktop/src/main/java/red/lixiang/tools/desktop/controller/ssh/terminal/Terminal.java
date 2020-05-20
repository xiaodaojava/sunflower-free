package red.lixiang.tools.desktop.controller.ssh.terminal;


import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.connection.channel.direct.PTYMode;
import net.schmizz.sshj.connection.channel.direct.Session;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import red.lixiang.tools.desktop.controller.ssh.SSHConfig;
import red.lixiang.tools.desktop.utils.FXThreadTools;
import red.lixiang.tools.jdk.os.OSTools;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import static red.lixiang.tools.desktop.controller.ssh.SSHConfig.AUTH_PASSWORD;

public class Terminal extends TerminalView {


    SSHClient ssh;
    Session session;

    private final ObjectProperty<Writer> outputWriterProperty;
    private final Path terminalPath;
    private String[] termCommand;
    private final LinkedBlockingQueue<String> commandQueue;

    public Terminal() {
        this(null, null);
    }

    public Terminal(TerminalConfig terminalConfig, Path terminalPath) {
        setTerminalConfig(terminalConfig);
        this.terminalPath = terminalPath;
        outputWriterProperty = new SimpleObjectProperty<>();
        commandQueue = new LinkedBlockingQueue<>();
    }

    /**
     * 发送出去的命令
     * @param command
     */
    @WebkitCall
    public void command(String command) {
        try {
            commandQueue.put(command);
        } catch (final InterruptedException e) {
            throw new RuntimeException(e);
        }
        FXThreadTools.start(() -> {
            try {
                final String commandToExecute = commandQueue.poll();
                getOutputWriter().write(commandToExecute);
                getOutputWriter().flush();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void onTerminalReady() {
        FXThreadTools.start(() -> {
            try {
                initializeProcess();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void initializeProcess() throws Exception {
//        final Path dataDir = getDataDir();
//        if (OSTools.isWin()) {
//            this.termCommand = getTerminalConfig().getWindowsTerminalStarter().split("\\s+");
//        } else {
//            this.termCommand = getTerminalConfig().getUnixTerminalStarter().split("\\s+");
//        }

        final Map<String, String> envs = new HashMap<>(System.getenv());
        envs.put("TERM", "xterm");


        //开始连接
        //开始连接
        try{
            ssh = new SSHClient();
            ssh.addHostKeyVerifier(new PromiscuousVerifier());
            ssh.connect(getConfig().getIp());
            if(AUTH_PASSWORD==getConfig().getAuthType()){

                ssh.authPassword(getConfig().getUsername(),getConfig().getPassword());
            }
            session = ssh.startSession();
            try {
//                session.allocateDefaultPTY();
                session.allocatePTY("vt100", 80, 24, 0, 0, Collections.<PTYMode, Integer>emptyMap());
                Session.Shell shell = session.startShell();
                outputStream = shell.getOutputStream();
                inputStream = shell.getInputStream();
                setInputReader(new BufferedReader(new InputStreamReader(inputStream)));
                setErrorReader(new BufferedReader(new InputStreamReader(shell.getErrorStream())));
                setOutputWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        focusCursor();


    }



    public Path getTerminalPath() {
        return terminalPath;
    }



    public ObjectProperty<Writer> outputWriterProperty() {
        return outputWriterProperty;
    }

    public Writer getOutputWriter() {
        return outputWriterProperty.get();
    }

    public void setOutputWriter(Writer writer) {
        outputWriterProperty.set(writer);
    }

    public Session getSession() {
        return session;
    }
    public SSHClient getSSHClient(){
        return ssh;
    }

    public SSHConfig getConfig(){
        return getTerminalConfig().getSshConfig();
    }

}
