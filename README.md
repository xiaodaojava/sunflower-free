# 说明

## 求打赏
生活窘迫,难以为继, 开源代码,谋几两碎银,谢谢您嘞~  

<img src="https://gitee.com/smeilknife/image1/raw/bb014793b5c3325ace30613702cf775d4fa16c38/20200325/1585138767636.jpeg" width="200"/>
<img src="https://gitee.com/smeilknife/image1/raw/master/2020/5/20/1589976429306.jpeg" width="200"/>

## 运行
jdk14, gradle 6.3
使用idea右侧gradle面板->tools-desktop->application-run

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