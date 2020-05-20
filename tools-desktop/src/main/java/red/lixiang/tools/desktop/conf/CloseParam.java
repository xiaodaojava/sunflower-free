package red.lixiang.tools.desktop.conf;

import red.lixiang.tools.common.redis.RedisCommonTools;

import java.util.HashMap;
import java.util.Map;

/**
 * 需要被关闭的一个资源
 * @author lixiang
 * @date 2020/3/15
 **/
public class CloseParam {

    public static   Map<String, RedisCommonTools> toolsMap  = new HashMap<>();


    public static void close(){
        toolsMap.forEach((x,y)->y.close());
    }
}
