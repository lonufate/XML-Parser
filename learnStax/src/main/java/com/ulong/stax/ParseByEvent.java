package com.ulong.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

/**
 * Stax使用迭代器风格的API
 *
 * @author Ulong
 * @create 2016/9/22
 */
public class ParseByEvent {
    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        // Use the reference implementation for the XML input factory
        System.setProperty("javax.xml.stream.XMLInputFactory", "com.bea.xml.stream.MXParserFactory");
        // Create the XML input factory
        XMLInputFactory factory = XMLInputFactory.newInstance();
        // Create the XML event reader
        FileReader reader = new FileReader("learnStax/src/main/resources/room.xml");
        XMLEventReader r = factory.createXMLEventReader(reader);
        // Loop over XML input stream and process events
        while(r.hasNext()) {
            XMLEvent e = (XMLEvent)r.next();
            processEvent(e);
        }
    }
    /**
     * Process a single XML event
     * @param e - the event to be processed
     */
    private static void processEvent(XMLEvent e) {
        if (e.isStartElement()) {
            QName qname = ((StartElement) e).getName();
            String namespaceURI = qname.getNamespaceURI();
            String localName = qname.getLocalPart();
            Iterator iter = ((StartElement) e).getAttributes();
            while (iter.hasNext()) {
                Attribute attr = (Attribute) iter.next();
                QName attributeName = attr.getName();
                String attributeValue = attr.getValue();
            }
        }
        if (e.isEndElement()) {
            QName qname = ((EndElement) e).getName();
        }
        if (e.isCharacters()) {
            String text = ((Characters) e).getData();
        }
        if (e.isStartDocument()) {
            String version = ((StartDocument) e).getVersion();
            String encoding = ((StartDocument) e).getCharacterEncodingScheme();
            boolean isStandAlone = ((StartDocument) e).isStandalone();
        }
    }
}
