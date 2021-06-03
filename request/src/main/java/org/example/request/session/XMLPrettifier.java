package org.example.request.session;


import com.sun.xml.bind.v2.runtime.XMLSerializer;
//import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

/**
 * Utility class for pretty printing the XML in logs.
 */
@SuppressWarnings("restriction")
public class XMLPrettifier {
//
//    public static String pretify(String unformattedXml) {
//        try {
//            final Document document = parseXmlFile(unformattedXml);
//
//            OutputFormat format = new OutputFormat(document);
//            format.setLineWidth(65);
//            format.setIndenting(true);
//            format.setIndent(2);
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(document);
//
//            return out.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static String pretify(InputStream inputStream) {
//        try {
//            final Document document = parseXmlFile(inputStream);
//
//            OutputFormat format = new OutputFormat(document);
//            format.setLineWidth(65);
//            format.setIndenting(true);
//            format.setIndent(2);
//            Writer out = new StringWriter();
//            XMLSerializer serializer = new XMLSerializer(out, format);
//            serializer.serialize(document);
//
//            return out.toString();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private static Document parseXmlFile(String in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(in));
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Document parseXmlFile(InputStream in) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(in);
            return db.parse(is);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}
