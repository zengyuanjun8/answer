#指定基础镜像，在其上进行定制
FROM jre:8

#这里的 /tmp 目录就会在运行时自动挂载为匿名卷，任何向 /tmp 中写入的信息都不会记录进容器存储层
VOLUME /tmp

#复制上下文目录下的target/demo-1.0.0.jar 到容器里
COPY target/answer-1.0.jar answer-1.0.jar

#bash方式执行，使demo-1.0.0.jar可访问
#RUN新建立一层，在其上执行这些命令，执行结束后， commit 这一层的修改，构成新的镜像。
#RUN bash -c "touch /answer-1.0.jar"

#指定容器启动程序及参数   <ENTRYPOINT> "<CMD>"
ENTRYPOINT ["java","-Xms128M","-Xmx128M","-Xss64M","-Dfile.encoding=UTF-8","-XX:MetaspaceSize=64M","-jar","answer-1.0.jar"]

#CMD会启动两个进程？
#CMD java -Xms128M -Xmx128M -Xss64M -Dfile.encoding=UTF-8 -XX:MetaspaceSize=64M -jar answer-1.0.jar