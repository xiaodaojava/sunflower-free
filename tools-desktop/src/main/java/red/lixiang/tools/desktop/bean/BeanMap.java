package red.lixiang.tools.desktop.bean;


import red.lixiang.tools.desktop.conf.FxBean;
import red.lixiang.tools.desktop.utils.ModuleTools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author lixiang
 * @date 2020/2/23
 **/
public class BeanMap {

    private static Map<Class<?>, Object> beanMap = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <T> T getBean(Class<T> clazz) {
        Object o = beanMap.get(clazz);

        return (T) o;
    }

    public static void main(String[] args) {
        initBean();
    }

    public static void initBean() {
        try {
            List<String> classPathList = ModuleTools.currentModuleClass();
            for (String classPath : classPathList) {
                if (classPath.equals("module-info.class")) {
                    continue;
                }
                //开始加载bean
                classPath = classPath.substring(0,classPath.lastIndexOf(".")).replaceAll("/", "\\.");
                Class<?> aClass = Class.forName(classPath);
                FxBean fxBean = aClass.getAnnotation(FxBean.class);
                if(fxBean==null){
                    continue;
                }
                beanMap.put(aClass, aClass.getDeclaredConstructor().newInstance());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> doList(Path path) {
        List<String> resList = new ArrayList<>();
        try (Stream<Path> list = Files.list(path)) {
            list.forEach(x -> {
                if (Files.isDirectory(x)) {
                    resList.addAll(doList(x));
                } else {
                    resList.add(x.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resList;
    }

}
