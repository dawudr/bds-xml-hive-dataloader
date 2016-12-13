package com.sainsburys;

import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;

public class XifLayer {

    public static void main(String[] args) {
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        XslTransformer xslt = new XslTransformer();

        String directory ="testSamples";
        directory =(System.getProperty("user.dir") + "/" + directory + "/");
        String inputFile = directory + "R10xml.xml";

        String resources ="src/main/resources";
        directory =(System.getProperty("user.dir") + "/" + resources + "/");
        String config = directory + "r10_pipeline.xsl";

        File xmlFile = new File(inputFile);
        File xslFile = new File(config);
        Writer writer = new StringWriter();
        StreamResult xmlOutput = new StreamResult(new StringWriter());

        try {
            xslt.process(xmlFile, xslFile, xmlOutput);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

        System.out.println(xmlOutput.getWriter().toString());

    }

}
