package com.sainsburys;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;


public class FileXmlDataLoader {

    public static void loadXMLfromFile(String filename) {

        //Grab Test XML message example from file
        //directory = System.getProperty("user.dir")
        System.out.println("Loading file: " + filename);

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder;
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filename));
            doc.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            String expression = "/POSLog/Transaction/CustomerOrderTransaction/LineItem/Sale";

            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node nNode = nodeList.item(i);
                System.out.println("\nCurrent Element :" + nNode.getNodeName());
//                System.out.println("All Element value:" + nNode.getTextContent());
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    System.out.println("MerchandiseHierarchy : " + eElement.getElementsByTagName("MerchandiseHierarchy").item(0).getTextContent());
                    System.out.println("Quantity : " + eElement.getElementsByTagName("Quantity").item(0).getTextContent());
                    System.out.println("ItemID  : " + eElement.getElementsByTagName("ItemID").item(0).getTextContent());
                    System.out.println("Description  : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
                    System.out.println("RegularSalesUnitPrice  : " + eElement.getElementsByTagName("RegularSalesUnitPrice").item(0).getTextContent());
                }
            }

        } catch (IOException e) {
            System.out.println("Unable to get file from directory");
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            System.out.println("Unable to parse file");
            e.printStackTrace();
        } catch (SAXException e) {
            System.out.println("Unable to parse invalid XML");
            e.printStackTrace();
        } catch (XPathExpressionException e) {
            System.out.println("Invalid XPath");
            e.printStackTrace();
        }
    }
}
