## Data Uploader for Xpath filtered data from XML file into Hive using Jdbc


This is a Java Application to read from Xpath in XML document and load into Hive via JDBC:

1. It is assumed that Maven is installed on your workstation. Ensure that maven is added to your PATH environment variable. Open a command prompt on your workstation and change folder to where you wish to create the project. For example, cd C:\Maven\MavenProjects

2. Use the mvn command, to generate the project template, as shown below –
```mvn archetype:generate -DgroupId=com.empeccable -DartifactId=XmlHiveDataloader -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false```

3. Build the project using maven command line:
    ```
    mvn clean package
    ```

4. Run the compiled JAR executable in target folder passing the full filename path of the data file.

```
C:\Users\dev\Documents\workspace\empeccable\bds-xml-hive-dataloader\target>java -jar XmlHiveDataloader-1.0-SNAPSHOT.jar ../testSamples/SampleLargeInputFormatA.xml
Loading file: ../testSamples/SampleLargeInputFormatA.xml
Current Element :Sale

ItemID  : 323628
Description  : JS ROYAL GALA MIN X5
RegularSalesUnitPrice  : 1.95
Current Element :Sale

ItemID  : 1857979
Description  : *TU SMTY KNIVES 4PK
RegularSalesUnitPrice  : 5
Current Element :Sale

ItemID  : 1857979
Description  : *TU SMTY KNIVES 4PK
RegularSalesUnitPrice  : 5
Current Element :Sale
```

## Getting Cloudera CDH image on Docker

https://hub.docker.com/r/cloudera/quickstart/
```
docker pull cloudera/quickstart:latest
docker images
```


Once you know the name or hash of the image, you can run it:

```
docker run --hostname=quickstart.cloudera --privileged=true -t -i [OPTIONS] [IMAGE] /usr/bin/docker-quickstart

Explanation for required flags and other options are in the following table:

--hostname=quickstart.cloudera    Required: pseudo-distributed configuration assumes this hostname
--privileged=true                 Required: for HBase, MySQL-backed Hive metastore, Hue, Oozie, Sentry, and Cloudera Manager, and possibly others
-t                                Required: once services are started, a Bash shell takes over and will die without this
-i                                Required: if you want to use the terminal, either immediately or attach later
-p 8888                           Recommended: maps the Hue port in the guest to another port on the host
-p [PORT]                         Optional: map any other ports (e.g. 7180 for Cloudera Manager, 80 for a guided tutorial)
-d                                Optional: runs the container in the background
```
/usr/bin/docker-quickstart is provided as a convenience to start all CDH services, then run a Bash shell. You can directly run /bin/bash instead if you wish to start services manually.


## Running Cloudera CDH
```
docker run -–privileged=true –-hostname=quickstart.cloudera -p 7180:7180 -p 8888:8888 -p 50070:50070 -p 50075:50075 -p 18080:18080 -p 18081:18081 -p 8088:8088 -p 8042:8042 -p 10020:10020 -p 8040:8040 -p 10000:10000 -p 9083:9083 -t -i 4239cd2958c6 /usr/bin/docker-quickstart
```
