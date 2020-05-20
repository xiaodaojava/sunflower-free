package red.lixiang.tools.desktop.conf;

import red.lixiang.tools.desktop.DesktopMain;

/**
 * 这个类是记录日志的,对日志做一个封装
 *
 * @author lixiang
 * @date 2020/3/27
 **/
public class DesktopLogger {

    public static final System.Logger logger= System.getLogger("DESKTOP");

    static {
        //对logger做一些配置
    }

    public static void info(String content,Object... objects){
        logger.log(System.Logger.Level.INFO,content,objects);
    }

    public static void main(String[] args) {
            info("hello");
    }

}
