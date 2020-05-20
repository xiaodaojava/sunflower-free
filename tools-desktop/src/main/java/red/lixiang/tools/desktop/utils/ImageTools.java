package red.lixiang.tools.desktop.utils;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import static red.lixiang.tools.desktop.conf.RunParam.ConfigDir;

/**
 * @author lixiang
 * @date 2020/3/18
 **/
public class ImageTools {
    public static File readFile(Image image){
        File temp = new File(ConfigDir+"temp.png");
        if(temp.exists()){
            temp.delete();
        }
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image,null);
        try {
            ImageIO.write(bufferedImage, "png", temp);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return temp;

    }

    public static Image createFromUrl(String url){

        URLConnection conn = null;
        try {
            conn = new URL(url).openConnection();
            conn.setRequestProperty("User-Agent", "Sunflower");
            try (InputStream stream = conn.getInputStream()) {
                return new Image(stream);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
