package red.lixiang.tools.desktop.controller.kub;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import red.lixiang.tools.common.kubernetes.KubernetesConfig;
import red.lixiang.tools.desktop.bean.BeanMap;
import red.lixiang.tools.desktop.conf.GlobalConfig;
import red.lixiang.tools.desktop.conf.RunParam;
import red.lixiang.tools.desktop.manager.kub.KubManager;
import red.lixiang.tools.desktop.utils.AlertTools;
import red.lixiang.tools.jdk.StringTools;
import javafx.event.ActionEvent;

import java.util.List;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
public class KubEditController {

    public GlobalConfig config =  GlobalConfig.getConfig();

    public TextField kubConfigTag;

    public TextArea kubConfigContent;

    private KubManager kubManager = BeanMap.getBean(KubManager.class);

    public void saveKubConfig(ActionEvent actionEvent) {
        String tag  = kubConfigTag.getText();
        String  content = kubConfigContent.getText();
        if(StringTools.isBlank(tag) || StringTools.isBlank(content)){
            AlertTools.popDialog("标签或内容不能为空");
            return;
        }
        List<KubernetesConfig> kubConfigList = config.getKubConfigList();
        for (KubernetesConfig config : kubConfigList) {
            if(tag.equals(config.getTag())){
                AlertTools.popDialog("标签重复");
                return;
            }
        }
        KubernetesConfig newConfig  = new KubernetesConfig();
        newConfig.setTag(tag);
        newConfig.loadConfigFromStr(content);
        kubConfigList.add(newConfig);
        kubManager.editKubConfig(newConfig);
        RunParam.RunParam().getKubEditStage().close();
    }
}
