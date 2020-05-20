package red.lixiang.tools.desktop.controller.ssh.terminal;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import netscape.javascript.JSObject;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.utils.FXThreadTools;
import red.lixiang.tools.desktop.utils.zmodem.ZModem;
import red.lixiang.tools.desktop.utils.zmodem.util.CustomFile;
import red.lixiang.tools.desktop.utils.zmodem.util.FileAdapter;
import red.lixiang.tools.jdk.FileTools;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;

public class TerminalView extends Pane {

    private final WebView webView;
    private final ReadOnlyIntegerWrapper columnsProperty;
    private final ReadOnlyIntegerWrapper rowsProperty;
    private final ObjectProperty<Reader> inputReaderProperty;
    private final ObjectProperty<Reader> errorReaderProperty;
    /**
     * shell的输出流
     */
    OutputStream outputStream;
    /**
     * shell的输入流
     */
    InputStream inputStream;


    private TerminalConfig terminalConfig = new TerminalConfig();
//	protected final CountDownLatch countDownLatch = new CountDownLatch(1);

    public TerminalView() {
        webView = new WebView();
        columnsProperty = new ReadOnlyIntegerWrapper(150);
        rowsProperty = new ReadOnlyIntegerWrapper(10);
        inputReaderProperty = new SimpleObjectProperty<>();
        errorReaderProperty = new SimpleObjectProperty<>();

        /**
         * 从shell 中拿到的数据
         */
        inputReaderProperty.addListener((observable, oldValue, newValue) -> {
            FXThreadTools.start(() -> printReader(newValue));
        });

        errorReaderProperty.addListener((observable, oldValue, newValue) -> {
            FXThreadTools.start(() -> {
                printReader(newValue);
            });
        });

        webView.getEngine().getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {

            getWindow().setMember("app", this);
        });
        webView.prefHeightProperty().bind(heightProperty());
        webView.prefWidthProperty().bind(widthProperty());

        webEngine().load(TerminalView.class.getResource("/tools/ssh/hterm.html").toExternalForm());
    }

    @WebkitCall(from = "hterm")
    public String getPrefs() {
        try {

            return new ObjectMapper().writeValueAsString(getTerminalConfig());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePrefs(TerminalConfig terminalConfig) {
        if (getTerminalConfig().equals(terminalConfig)) {
            return;
        }

        setTerminalConfig(terminalConfig);
        final String prefs = getPrefs();

        FXThreadTools.runActionLater(() -> {
            try {
                getWindow().call("updatePrefs", prefs);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }, true);
    }

    @WebkitCall(from = "hterm")
    public void resizeTerminal(int columns, int rows) {
        columnsProperty.set(columns);
        rowsProperty.set(rows);
    }

    @WebkitCall
    public void onTerminalInit() {
        System.out.println("on init");
        FXThreadTools.runActionLater(() -> {
            getChildren().add(webView);
        }, true);
    }

    /**
     * Internal use only
     */
    public void onTerminalReady() {

    }

    private void printReader(Reader bufferedReader) {
        try {
            int nRead;
            final char[] data = new char[1024];


            while ((nRead = bufferedReader.read(data, 0, data.length)) != -1) {
                final StringBuilder builder = new StringBuilder(nRead);
                builder.append(data, 0, nRead);
                String string = builder.toString();

                if (string.contains("rz waiting to receive")) {
                    //需要上传文件的
					print("文件正在上传中,请稍后...\r\n");
                    Platform.runLater(()->{
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle("选择要上传的文件");
                        File file = chooser.showOpenDialog(RunParam.RunParam().getCurrentStage());
                        var filePath = file.getAbsolutePath();
                        ZModem modem = new ZModem(inputStream, outputStream);
                        try {
                            Map<String, FileAdapter> map = new HashMap<>();
                            map.put(FileTools.getFileNameFromPath(filePath),new CustomFile(file));
                            modem.send(map);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

				} else if (string.contains("rz\r**\u0018B000000")) {
                    // 需要本机接收文件的
					print("文件正在传输中,请稍后...\r\n");
					Platform.runLater(()->{
                        DirectoryChooser chooser = new DirectoryChooser();
                        chooser.setTitle("选择要下载文件的位置");
                        File file = chooser.showDialog(RunParam.RunParam().getCurrentStage());
                        ZModem modem = new ZModem(inputStream, outputStream);
                        try {
                            modem.receive(new CustomFile(file));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    print(builder.toString());
                }
            }


        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    @WebkitCall(from = "hterm")
    public void copy(String text) {
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(text);
        clipboard.setContent(clipboardContent);
    }


    /**
     * 把接收到的内容,输出到页面上
     *
     * @param text
     */
    protected void print(String text) {
//		FXThreadTools.awaitLatch(countDownLatch);
        FXThreadTools.runActionLater(() -> {
            getTerminalIO().call("print", text);
        });

    }

    public void focusCursor() {
        FXThreadTools.runActionLater(() -> {
            webView.requestFocus();
            getTerminal().call("focus");
        }, true);
    }

    private JSObject getTerminal() {
        return (JSObject) webEngine().executeScript("t");
    }

    private JSObject getTerminalIO() {
        return (JSObject) webEngine().executeScript("t.io");
    }

    public JSObject getWindow() {
        return (JSObject) webEngine().executeScript("window");
    }

    private WebEngine webEngine() {
        return webView.getEngine();
    }

    public TerminalConfig getTerminalConfig() {
        if (Objects.isNull(terminalConfig)) {
            terminalConfig = new TerminalConfig();
        }
        return terminalConfig;
    }

    public void setTerminalConfig(TerminalConfig terminalConfig) {
        this.terminalConfig = terminalConfig;
    }

    public ReadOnlyIntegerProperty columnsProperty() {
        return columnsProperty.getReadOnlyProperty();
    }

    public int getColumns() {
        return columnsProperty.get();
    }

    public ReadOnlyIntegerProperty rowsProperty() {
        return rowsProperty.getReadOnlyProperty();
    }

    public int getRows() {
        return rowsProperty.get();
    }

    public ObjectProperty<Reader> inputReaderProperty() {
        return inputReaderProperty;
    }

    public Reader getInputReader() {
        return inputReaderProperty.get();
    }

    public void setInputReader(Reader reader) {
        inputReaderProperty.set(reader);
    }

    public ObjectProperty<Reader> errorReaderProperty() {
        return errorReaderProperty;
    }

    public Reader getErrorReader() {
        return errorReaderProperty.get();
    }

    public void setErrorReader(Reader reader) {
        errorReaderProperty.set(reader);
    }

}
