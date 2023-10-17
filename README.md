<div align="center"> <img src="https://res.ztion.cn/imgs/1697525163723.png" width = 200 /> </div> </div>

<p align="center">
  Focus 焦距：手机与PC短信通知同步工具
</p>


# 简介

是个网站现在登录都要验证手机验证码，用电脑的时候，每次都要找到打开手机，很麻烦，遂做此工具，手机推送短信到服务端，PC安装客户端后，服务端通过ws将短信推送到PC端，PC端将短信推送到系统通知。



# 截图

![image-20231017154329781](https://res.ztion.cn/imgs/1697527924592.png)

# 技术
Android端：Java
后台服务端：Java+Springboot
PC端：Electron+Vue3+ElementPlus

Electron打包后自带内核太重了，一个小工具都这么大，有空用rust+slint试试
# 原理
服务端提供一个POST接口，安卓端收到短信时，将短信内容提交到服务端，服务端将短信内容推送到PC端，PC端将短信推送到系统通知。

![image-20231017152409341](https://res.ztion.cn/imgs/1697526762896.png)



# 声明

- 软件涉及到的引用的图片以及影音均来自网络，侵删，仅限于个人学习使用！请勿用于任何商业用途
-
# 版权

[GPL 3.0](https://www.gnu.org/licenses/gpl-3.0.html)

# 作者

ZtionJam,工地搬砖

# 联系方式

微信:

![image-20230118211145845](https://res.ztion.cn/imgs/1674047507177.png)
