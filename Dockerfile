FROM openjdk:8
RUN mkdir /root/app
COPY BankProject-1.0-SNAPSHOT.jar /root/app/Bank_API.jar
RUN curl -k -O https://h2database.com/h2-2019-03-13.zip
RUN unzip h2*.zip
COPY BankTask.mv.db /root/BankTask.mv.db
CMD ["java", "-jar", "/root/app/Bank_API.jar"]