set JAVA_HOME=%JAVA_8_201_HOME%
set Path=%JAVA_HOME%/bin;%Path%
REM mvn -v
mvn clean package -DskipTests