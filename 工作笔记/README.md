
###常用代码（idea为例）：
 
 ````
  
  终端启动tomcat并输出日志方法：
  1，cd 到tomcat目录：
  /Users/digimagus/Documents/tool/tomcat-8.5.29/bin
  2，改变文件权限：
  sudo chmod 755 *.sh
  3，启动并输出日志：
   sudo  sh catalina.sh run
   
 ````
### mac maven安装本地jar包到本地仓库

 ````
  
 1，安装maven
    1.1 下载：http://maven.apache.org/download.cgi，并解压
    1.2 open ~/.bash_profile
    1.3 添加如下两行
      export M2_HOME=/Users/xxx/Documents/maven/apache-maven-3.5.0
      source ~/.bash_profile
    1.4 检查是否安装成功
      mvn -v    
 2，安装本地jar:
 mvn install:install-file -DgroupId=cc.landking.web -DartifactId=utils -Dversion=5.0.0 -Dpackaging=jar -Dfile=/Users/digimagus/Documents/公司项目/张平代码/消检通/v3.5/xjt/src/main/webapp/WEB-INF/lib/utils-5.0.0.jar 
   
   参数说明：
     -Dfile= jar路径
     -Dversion= pom.xml中的版本号
     -DartifactId=pom.xml中的artifactId
     -DgroupId=pom.xml中的groupId
     
 ````