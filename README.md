# 说明


## 运行
jdk17, gradle 7.5

使用idea右侧gradle面板->tools-desktop->application-run

## 打包
修改gradle版本
./gradlew wrapper --gradle-version=7.5


### mac 
```shell script

/Users/lixiang/soft/jdk-17.jdk/Contents/Home/bin/jpackage  --runtime-image image  \
--type dmg --name Sunflower \
--icon ../Sunflower_icon.icns \
--java-options --enable-preview \
--module red.lixiang.tools.desktopmain/red.lixiang.tools.desktop.DesktopMain


```

### windows
windows jlink , modify Sunflower.bat add --enable-preview 
C:\soft\jdk-17\bin\jpackage --java-options --enable-preview --icon ../Sunflower.ico  --runtime-image image  --type app-image --name Sunflower  --module red.lixiang.tools.desktopmain/red.lixiang.tools.desktop.DesktopMain