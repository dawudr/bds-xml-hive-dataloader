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
import java.sql.SQLException;


public class FileXmlDataLoader {

    public static void loadXMLfromFile(String filename) {

        //Grab Test XML message example from file
        //directory = System.getProperty("user.dir")
        System.out.println("Loading file: " + filename);
        HiveJdbcDataLoader jdbcDataLoader = null;

        try {

            jdbcDataLoader = new HiveJdbcDataLoader();

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
                    String merchandiseHierarchy = eElement.getElementsByTagName("MerchandiseHierarchy/@").item(0).getTextContent();
                    String quantity = eElement.getElementsByTagName("Quantity").item(0).getTextContent();
                    String itemID = eElement.getElementsByTagName("ItemID").item(0).getTextContent();
                    String description = eElement.getElementsByTagName("Description").item(0).getTextContent();
                    String regularSalesUnitPrice = eElement.getElementsByTagName("RegularSalesUnitPrice").item(0).getTextContent();

                    System.out.println("MerchandiseHierarchy : " + merchandiseHierarchy);
                    System.out.println("Quantity : " + quantity);
                    System.out.println("ItemID  : " + itemID);
                    System.out.println("Description  : " + description);
                    System.out.println("RegularSalesUnitPrice  : " + regularSalesUnitPrice);

                    jdbcDataLoader.insertIntoHive(merchandiseHierarchy, itemID, description, regularSalesUnitPrice, quantity);
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
        } catch (SQLException e) {
            System.out.println("Unable to insert recored into Hive");
        } finally {
            try {
                jdbcDataLoader.cleanup();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
