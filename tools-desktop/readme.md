# 说明

## 打包
修改gradle版本
./gradlew wrapper --gradle-version=6.3


### mac 
```shell script

/Users/lixiang/soft/jdk-14.jdk/Contents/Home/bin/jpackage  --runtime-image image  \
--type dmg --name Sunflower \
--icon ../Sunflower_icon.icns \
--java-options --enable-preview \
--module red.lixiang.tools.desktopmain/red.lixiang.tools.desktop.DesktopMain


```

### windows
windows jlink , modify Sunflower.bat add --enable-preview 
C:\soft\jdk-14\bin\jpackage --java-options --enable-preview --icon ../Sunflower.ico  --runtime-image image  --type app-image --name Sunflower  --module red.lixiang.tools.desktopmain/red.lixiang.tools.desktop.DesktopMain