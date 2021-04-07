FROM tomcat:9.0-jdk16-openjdk
ENV dbDriver=com.mysql.cj.jdbc.Driver dbConnectionUrl=jdbc:mysql://mysqldb:3306/atm dbUserName=root dbPassword=root
ADD ATM_Assessment.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD catalina.sh run

