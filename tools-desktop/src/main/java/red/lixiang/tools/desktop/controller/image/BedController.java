package red.lixiang.tools.desktop.controller.image;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import red.lixiang.tools.common.github.GitConfig;
import red.lixiang.tools.common.github.GitTools;
import red.lixiang.tools.common.github.model.ImageBed;
import red.lixiang.tools.common.github.model.UploadFileReq;
import red.lixiang.tools.common.github.model.UploadFileRes;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.utils.*;
import red.lixiang.tools.jdk.FileTools;
import red.lixiang.tools.jdk.RandomTools;
import red.lixiang.tools.jdk.StringTools;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lixiang
 * @date 2020/3/16
 **/
public class BedController implements Initializable {

    public ListView<BedCell> configListField;
    public AnchorPane bedPane;
    public FlowPane imageFlowPane;

    private ToggleGroup configGroup;

    ObservableList<BedCell> observableGitList;

    public TextField markdownField;
    public TextField cdnField;
    public TextField originField;

    private Integer historyPage=1;

    /**
     * 更新历史上传数据
     */
    public void uploadHistory(GitConfig config,boolean more){
        if(!more){
            historyPage = 1;
            imageFlowPane.getChildren().clear();
        }

        FXThreadTools.start(()->{
            List<String> urlList = GitTools.commitFileUrlList(config,historyPage);
            for (String imageUrl : urlList) {
                Image image = ImageTools.createFromUrl(imageUrl);
                ImageView imageView = new ImageView(image);
                imageView.setFitHeight(100);
                imageView.setFitWidth(100);
                imageView.setPickOnBounds(true);
                imageView.setOnMouseClicked(event -> {
                    if(event.getClickCount()==2){
                        //双击了两次,弹一个框框出来

                    }
                    //转成图床,显示在界面上
                    ImageBed imageBed = GitTools.convertUploadFileRes(imageUrl);
                    FXThreadTools.runActionLater(()->{
                        originField.setText(imageBed.getOriginUrl());
                        markdownField.setText(imageBed.getMarkdownUrl());
                        cdnField.setText(imageBed.getCdnMarkdownUrl());
                    });
                });
                FXThreadTools.runActionLater(()->{
                    imageFlowPane.getChildren().add(imageView);
                });
            }

            // 添加一个更多按钮
            Button moreBtn = new Button("更多");
            moreBtn.setOnAction(event -> {
//            GitConfig config = configListField.getSelectionModel().getSelectedItem().config;
                uploadHistory(config,true);
                imageFlowPane.getChildren().remove(moreBtn);
            });
            historyPage++;
            FXThreadTools.runActionLater(()->{
                imageFlowPane.getChildren().add(moreBtn);
            });

        });

    }

    /**
     * 从粘贴板上选择图片
     * @param actionEvent
     * @throws IOException
     */
    public void fromClipBoard(ActionEvent actionEvent) throws IOException {
        clear();
        Clipboard clipboard = Clipboard.getSystemClipboard();
        Set<DataFormat> contentTypes = clipboard.getContentTypes();
        //先尝试获取图片
        if (clipboard.hasImage()) {
            Image image = clipboard.getImage();
            File file = ImageTools.readFile(image);
            dealFile(file);
            file.delete();
        }else {
            List<File> files = clipboard.getFiles();
            for (File file : files) {
                //如果有多个文件的时候,只取第一个文件
                dealFile(file);
                break;
            }
        }

    }

    /**
     * 弹框自己选择图片
     * @param actionEvent
     */
    public void fromSelectedFile(ActionEvent actionEvent) {
        clear();
        FileChooser chooser = new FileChooser();
        chooser.setTitle("选择图片");
        chooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.jpeg"));
        File file = chooser.showOpenDialog(RunParam.RunParam().getMainStage());
        if (file != null) {
            dealFile(file);
        }
    }


    /**
     * 处理这个文件
     * @param file
     */
    public void dealFile(File file) {
        //获取配置
        GitConfig currentConfig = validConfig();
        // 上传文件
        UploadFileRes uploadFileRes = GitTools.uploadFile(file, currentConfig);
        ImageBed imageBed = GitTools.convertUploadFileRes(uploadFileRes.getContent().getDownloadUrl());
        originField.setText(imageBed.getOriginUrl());
        markdownField.setText(imageBed.getMarkdownUrl());
        cdnField.setText(imageBed.getCdnMarkdownUrl());
    }

    public void clear() {
        originField.setText("");
        markdownField.setText("");
        cdnField.setText("");
    }


    public BedCell buildCell(GitConfig config) {
        BedCell cell = new BedCell(config.getTag(), config);
        cell.check.setToggleGroup(configGroup);
        cell.check.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            if(event.getButton()== MouseButton.PRIMARY){
                cell.config.setSelected(cell.check.isSelected());
                saveConfigToLocal();
            }
        });
//        cell.edit.setOnAction(event -> {
//            TextInputDialog dialog = new TextInputDialog(cell.label.getText());
//            dialog.setContentText("新的标签名:");
//            Optional<String> result = dialog.showAndWait();
//            result.ifPresent(name -> {
//                cell.label.setText(name);
//                config.setTag(name);
//            });
//            saveConfigToLocal();
//        });
//        // 点删除的时候
//        cell.remove.setOnAction(event -> {
//            Alert alert = AlertTools.confirmAlert("是否确认删除这个图床?");
//            Optional<ButtonType> result = alert.showAndWait();
//            if (result.get() == ButtonType.OK) {
//                // 点了删除的时候. 移除当前的cell
//                observableGitList.remove(cell);
//                //  保存host
//                saveConfigToLocal();
//
//            }
//        });
        return cell;
    }


    /**
     * 新增一个配置,弹出一个页面
     * @param actionEvent
     */
    public void newConfig(ActionEvent actionEvent) {
        //新增的
        FXMLLoader loader = FXMLTools.initLoader("/tools/image/new.fxml");
        Stage stage = FXMLTools.initStage(loader.getRoot(), 800d, 500d);
        NewController controller = loader.getController();
        controller.init(stage,config -> {
            observableGitList.add(buildCell(config));
            saveConfigToLocal();
            stage.close();
        });
        stage.showAndWait();
    }

    /**
     * 把配置同步到全局配置中,在程序退出的时候,写入本地
     */
    public void saveConfigToLocal() {
        List<GitConfig> configList = observableGitList.stream().map(x -> x.config).collect(Collectors.toList());
        GlobalConfig.getConfig().setGitConfigList(configList);
    }

    public GitConfig validConfig(){
        List<GitConfig> gitConfigList = GlobalConfig.getConfig().getGitConfigList();
        GitConfig targetConfig= null;
        for (GitConfig config : gitConfigList) {
            if(config.getSelected()){
                targetConfig = config;
                break;
            }
        }
        return targetConfig;
    }
    public void copyImageUrl(MouseEvent mouseEvent) {
        NoticeTools.notice("已复制","已复制");
        var source = (TextField) mouseEvent.getSource();
        var clipboard = Clipboard.getSystemClipboard();
        var content = new ClipboardContent();
        content.putString(source.getText());
        clipboard.setContent(content);
        source.selectAll();
        RunParam.RunParam().getMainStage().requestFocus();
    }

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  {@code null} if the location is not known.
     * @param resources The resources used to localize the root object, or {@code null} if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {


        List<GitConfig> gitConfigList = GlobalConfig.getConfig().getGitConfigList();

        List<BedCell> bedCellList = gitConfigList.stream().map(this::buildCell).collect(Collectors.toList());

        observableGitList = FXCollections.observableList(bedCellList);
        configListField.setItems(observableGitList);
        configListField.getSelectionModel()
                .selectedItemProperty()
                .addListener((ov, oldVal, newVal) -> {
                    GitConfig config = newVal.config;
                    //这时候应该去找最近上传的
                    uploadHistory(config,false);
                });
        configListField.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> {
            if(event.getButton()== MouseButton.PRIMARY && event.getClickCount() ==2){
                //双击的事件
                GitConfig config = configListField.getSelectionModel().getSelectedItem().config;
                uploadHistory(config,false);
            }
        });

    }


}
