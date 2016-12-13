package com.sainsburys;

import net.sf.saxon.*;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.pattern.NodeKindTest;
import net.sf.saxon.sxpath.XPathExpression;
import net.sf.saxon.xpath.XPathEvaluator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.*;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.net.URL;
import java.util.Properties;


/** Creates an XSLT transformer for processing an XML document.
 *  A new transformer, along with a style template are created
 *  for each document transformation. The XSLT, DOM, and
 *  SAX processors are based on system default parameters.
 */

public class XslTransformer {
    private TransformerFactory factory;

    public XslTransformer() {
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        factory =  TransformerFactory.newInstance();
        // set the TransformFactory to use the Saxon TransformerFactoryImpl method
    }

    /** Transform an XML and XSL document as <code>Reader</code>s,
     *  placing the resulting transformed document in a
     *  <code>Writer</code>. Convenient for handling an XML
     *  document as a String (<code>StringReader</code>) residing
     in memory, not on disk. The output document could easily
     be
     *  handled as a String (<code>StringWriter</code>) or as a
     *  <code>JSPWriter</code> in a JavaServer page.
     */

    public void process(Reader xmlFile, Reader xslFile,
                        Writer output)
            throws TransformerException {
        process(new StreamSource(xmlFile),
                new StreamSource(xslFile),
                new StreamResult(output));
    }

    /** Transform an XML and XSL document as <code>File</code>s,
     *  placing the resulting transformed document in a
     *  <code>Writer</code>. The output document could easily
     *  be handled as a String (<code>StringWriter</code)> or as
     *  a <code>JSPWriter</code> in a JavaServer page.
     */
    public void process(File xmlFile, File xslFile,
                        Writer output)
            throws TransformerException {
        process(new StreamSource(xmlFile),
                new StreamSource(xslFile),
                new StreamResult(output));
    }

    /** Transform an XML <code>File</code> based on an XSL
     *  <code>File</code>, placing the resulting transformed
     *  document in a <code>OutputStream</code>. Convenient for
     *  handling the result as a <code>FileOutputStream</code> or
     *  <code>ByteArrayOutputStream</code>.
     */

    public void process(File xmlFile, File xslFile,
                        OutputStream out)
            throws TransformerException {
        process(new StreamSource(xmlFile),
                new StreamSource(xslFile),
                new StreamResult(out));
    }


    public void process(File xmlFile, File xslFile,
                        StreamResult out)
            throws TransformerException {
        process(new StreamSource(xmlFile),
                new StreamSource(xslFile),
                out);
    }

    /** Transform an XML source using XSLT based on a new template
     *  for the source XSL document. The resulting transformed
     *  document is placed in the passed in <code>Result</code>
     *  object.
     */

    public void process(Source xml, Source xsl, Result result)
            throws TransformerException {
        try {
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            Templates template = factory.newTemplates(xsl);
            Transformer transformer = template.newTransformer();
            transformer.transform(xml, result);
        } catch(TransformerConfigurationException tce) {
            throw new TransformerException(
                    tce.getMessageAndLocation());
        } catch (TransformerException te) {
            throw new TransformerException(
                    te.getMessageAndLocation());
        }
    }
}