FROM guoshengkai/openjdk:17
COPY  ChatGPT_CN_Client-0.0.1-SNAPSHOT.jar /home/apps/ChatGPT_CN_Client-0.0.1-SNAPSHOT.jar
COPY entrypoint.sh /home/apps/entrypoint.sh
WORKDIR /home/apps
EXPOSE 8080
# 启动nginx和java
ENTRYPOINT ["sh", "entrypoint.sh"]
#ENTRYPOINT ["java", "-jar", "chatgpt-0.0.1-SNAPSHOT.jar"]