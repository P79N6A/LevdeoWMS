前端
1、constant.js："printerIP": "172.17.4.95"。
条码打印机的ip地址，根据实际情况设定。

========================================================================================================================

后台
1、application.yml中配置配置文件。
开发环境： spring.profiles.active --> dev
生产环境： spring.profiles.active --> product

2、项目运行后，自动创建文件夹，路径配置。
条码路径：application-dev.yml（application-product.yml） --> my.barcodepath。
文件默认上传路径：application-dev.yml（application-product.yml） --> spring.servlet.multipart.location
log路径：logback-spring-dev.xml（logback-spring-product.xml） --> <property name="log_dir" value="d:/LevdeoWms/logs"/>

3、Linux部署后，
   a、创建共享jar包
     tomcat文件夹下新建share文件夹，将 libsapjco3.so、sapjco3.jar 拷贝进来。
     打进入tomcat/conf/目录,修改catalina.properties配置文件，找到shared.loader=位置，设置共享目录地址。
     本次设置的是
     shared.loader=/usr/local/apache-tomcat-8.5.38/share,/usr/local/apache-tomcat-8.5.38/share/*.jar
   b、（这一步可能不需要）
     需要把 /usr/local/apache-tomcat-8.5.38/share/libsapjco3.so 文件，
     拷贝到 /usr/local/apache-tomcat-8.5.38/webapps/LevdeoWMSWebService/WEB-INF/lib/libsapjco3.so

========================================================================================================================

PDA
1、PDA无法通过USB连接电脑
   解决方案：
     服务里面开启“Windows Mobile 2003 ベース デバイスの接続”“Windows Mobile ベース デバイスの接続”。
     两个服务的登录窗口，都选择无用户名密码的方式。

2、PDACar2019，修改Config.xml的WebUrl