Developing a Java Application to read from Xpath in XML document and load into Hive via JDBC:

1. It is assumed that Maven is installed on your workstation. Ensure that maven is added to your PATH environment variable. Open a command prompt on your workstation and change folder to where you wish to create the project. For example, cd C:\Maven\MavenProjects

2. Use the mvn command, to generate the project template, as shown below –
```mvn archetype:generate -DgroupId=com.sainsburys -DartifactId=XmlHiveDataloader -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false```

3. Build the project using maven command line:
    ```
    cd c:\Maven\MavenProjects\HiveJdbctest
    mvn clean package
    ```
    Once it builds successfully, we can step through and debug the code in IDE.