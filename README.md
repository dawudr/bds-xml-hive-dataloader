## Data Uploader for Xpath filtered data from XML file into Hive using Jdbc


This is a Java Application to read from Xpath in XML document and load into Hive via JDBC:

1. It is assumed that Maven is installed on your workstation. Ensure that maven is added to your PATH environment variable. Open a command prompt on your workstation and change folder to where you wish to create the project. For example, cd C:\Maven\MavenProjects

2. Use the mvn command, to generate the project template, as shown below –
```mvn archetype:generate -DgroupId=com.sainsburys -DartifactId=XmlHiveDataloader -DarchetypeArtifactId=maven-archetype-quickstart -DinteractiveMode=false```

3. Build the project using maven command line:
    ```
    mvn clean package
    ```

4. Run the compiled JAR executable in target folder passing the full filename path of the data file.

```
C:\Users\dev\Documents\workspace\sainsburys\bds-xml-hive-dataloader\target>java -jar XmlHiveDataloader-1.0-SNAPSHOT.jar ../testSamples/SampleLargeInputFormatA.xml
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