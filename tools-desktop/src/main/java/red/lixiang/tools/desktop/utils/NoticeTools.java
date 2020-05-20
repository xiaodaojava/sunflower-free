package red.lixiang.tools.desktop.utils;

import javafx.util.Duration;
import red.lixiang.tools.desktop.utils.notification.animations.Animations;
import red.lixiang.tools.desktop.utils.notification.notification.Notification;
import red.lixiang.tools.desktop.utils.notification.notification.Notifications;
import red.lixiang.tools.desktop.utils.notification.notification.TrayNotification;

/**
 * @author lixiang
 * @date 2020/3/25
 **/
public class NoticeTools {

    public static void notice(String title, String message){
        Notification notification = Notifications.SUCCESS;
        TrayNotification tray = new TrayNotification();
        tray.setTitle(title);
        tray.setMessage(message);
        tray.setNotification(notification);
        tray.setAnimation(Animations.FADE);
        tray.showAndDismiss(Duration.seconds(3));
    }
}
