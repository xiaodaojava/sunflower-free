package red.lixiang.tools.desktop.controller.ssh.terminal;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.scene.paint.Color;
import red.lixiang.tools.desktop.controller.ssh.SSHConfig;

import java.util.Objects;

/**
 * Created by usta on 12.09.2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TerminalConfig {

    @JsonProperty("use-default-window-copy")
    private boolean useDefaultWindowCopy = true;

    @JsonProperty("clear-selection-after-copy")
    private boolean clearSelectionAfterCopy = true;

    @JsonProperty("copy-on-select")
    private boolean copyOnSelect = false;

    @JsonProperty("ctrl-c-copy")
    private boolean ctrlCCopy = true;

    @JsonProperty("ctrl-v-paste")
    private boolean ctrlVPaste = true;

    @JsonProperty("cursor-color")
    private String cursorColor = "black";

    @JsonProperty(value = "background-color")
    private String backgroundColor = "white";

    @JsonProperty("font-size")
    private int fontSize = 14;

    @JsonProperty(value = "foreground-color")
    private String foregroundColor = "black";

    @JsonProperty("cursor-blink")
    private boolean cursorBlink = false;

    @JsonProperty("scrollbar-visible")
    private boolean scrollbarVisible = true;

    @JsonProperty("enable-clipboard-notice")
    private boolean enableClipboardNotice = true;

    @JsonProperty("scroll-wheel-move-multiplier")
    private double scrollWhellMoveMultiplier = 0.1;

    @JsonProperty("font-family")
    private String fontFamily = "\"DejaVu Sans Mono\", \"Everson Mono\", FreeMono, \"Menlo\", \"Terminal\", monospace";

    @JsonProperty(value = "user-css")
    private String userCss = "data:text/plain;base64," + "eC1zY3JlZW4geyBjdXJzb3I6IGF1dG87IH0=";

    @JsonIgnore
    private String windowsTerminalStarter = "cmd.exe";

    @JsonIgnore
    private String unixTerminalStarter = "/bin/bash -i";

    @JsonIgnore
    private SSHConfig sshConfig;

    public SSHConfig getSshConfig() {
        return sshConfig;
    }

    public TerminalConfig setSshConfig(SSHConfig sshConfig) {
        this.sshConfig = sshConfig;
        return this;
    }

    public boolean isUseDefaultWindowCopy() {
        return useDefaultWindowCopy;
    }

    public void setUseDefaultWindowCopy(boolean useDefaultWindowCopy) {
        this.useDefaultWindowCopy = useDefaultWindowCopy;
    }

    public boolean isClearSelectionAfterCopy() {
        return clearSelectionAfterCopy;
    }

    public void setClearSelectionAfterCopy(boolean clearSelectionAfterCopy) {
        this.clearSelectionAfterCopy = clearSelectionAfterCopy;
    }

    public boolean isCopyOnSelect() {
        return copyOnSelect;
    }

    public void setCopyOnSelect(boolean copyOnSelect) {
        this.copyOnSelect = copyOnSelect;
    }

    public boolean isCtrlCCopy() {
        return ctrlCCopy;
    }

    public void setCtrlCCopy(boolean ctrlCCopy) {
        this.ctrlCCopy = ctrlCCopy;
    }

    public boolean isCtrlVPaste() {
        return ctrlVPaste;
    }

    public void setCtrlVPaste(boolean ctrlVPaste) {
        this.ctrlVPaste = ctrlVPaste;
    }

    public String getCursorColor() {
        return cursorColor;
    }

    public void setCursorColor(String cursorColor) {
        this.cursorColor = cursorColor;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(String foregroundColor) {
        this.foregroundColor = foregroundColor;
    }

    public boolean isCursorBlink() {
        return cursorBlink;
    }

    public void setCursorBlink(boolean cursorBlink) {
        this.cursorBlink = cursorBlink;
    }

    public boolean isScrollbarVisible() {
        return scrollbarVisible;
    }

    public void setScrollbarVisible(boolean scrollbarVisible) {
        this.scrollbarVisible = scrollbarVisible;
    }

    public double getScrollWhellMoveMultiplier() {
        return scrollWhellMoveMultiplier;
    }

    public void setScrollWhellMoveMultiplier(double scrollWhellMoveMultiplier) {
        this.scrollWhellMoveMultiplier = scrollWhellMoveMultiplier;
    }

    public String getUserCss() {
        return userCss;
    }

    public void setUserCss(String userCss) {
        this.userCss = userCss;
    }

    public String getWindowsTerminalStarter() {
        return windowsTerminalStarter;
    }

    public void setWindowsTerminalStarter(String windowsTerminalStarter) {
        this.windowsTerminalStarter = windowsTerminalStarter;
    }

    public String getUnixTerminalStarter() {
        return unixTerminalStarter;
    }

    public void setUnixTerminalStarter(String unixTerminalStarter) {
        this.unixTerminalStarter = unixTerminalStarter;
    }

    public void setBackgroundColor(Color color) {
        setBackgroundColor(FxHelper.colorToHex(color));
    }

    public void setForegroundColor(Color color) {
        setForegroundColor(FxHelper.colorToHex(color));
    }

    public void setCursorColor(Color color) {
        setCursorColor(FxHelper.colorToHex(color));
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public boolean isEnableClipboardNotice() {
        return enableClipboardNotice;
    }

    public void setEnableClipboardNotice(boolean enableClipboardNotice) {
        this.enableClipboardNotice = enableClipboardNotice;
    }



    @Override
    public int hashCode() {
        return Objects.hash(useDefaultWindowCopy, clearSelectionAfterCopy, copyOnSelect, ctrlCCopy, ctrlVPaste, cursorColor, backgroundColor, fontSize, foregroundColor, cursorBlink, scrollbarVisible, enableClipboardNotice, scrollWhellMoveMultiplier, fontFamily, userCss, windowsTerminalStarter, unixTerminalStarter);
    }
}
