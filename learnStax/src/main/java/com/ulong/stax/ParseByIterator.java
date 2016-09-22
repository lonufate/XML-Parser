package com.ulong.stax;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Stax基于指针风格的API
 * XMLStreamReader类型的API next()方法提交一个整数值（而不是整个event对象）代表事件类型
 * 用XMLStreamReader解析文档要比XMLEventReader快约30%
 *
 * @author Ulong
 * @create 2016/9/22
 */
public class ParseByIterator {
    public static void main(String[] args)
            throws FileNotFoundException, XMLStreamException {
        // Use reference implementation
        /*System.setProperty("javax.xml.stream.XMLInputFactory", "com.bea.xml.stream.MXParserFactory");*/
        // Create an input factory
        XMLInputFactory xmlif = XMLInputFactory.newInstance();
        // Create an XML stream reader
        XMLStreamReader xmlr = xmlif.createXMLStreamReader(new FileReader("learnStax/src/main/resources/room.xml"));
        // Loop over XML input stream and process events
        while (xmlr.hasNext()) {
            processEvent(xmlr);
            xmlr.next();
        }
    }

    /**
     * Process a single event
     *
     * @param xmlr - the XML stream reader
     */
    private static void processEvent(XMLStreamReader xmlr) {
        switch (xmlr.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
                processName(xmlr);
                processAttributes(xmlr);
                break;
            case XMLStreamConstants.END_ELEMENT:
                processName(xmlr);
                break;
            case XMLStreamConstants.SPACE:
            case XMLStreamConstants.CHARACTERS:
                int start = xmlr.getTextStart();
                int length = xmlr.getTextLength();
                String text = new String(xmlr.getTextCharacters(), start, length);
                break;
            case XMLStreamConstants.COMMENT:
            case XMLStreamConstants.PROCESSING_INSTRUCTION:
                if (xmlr.hasText()) {
                    String piOrComment = xmlr.getText();
                }
                break;
        }
    }

    private static void processName(XMLStreamReader xmlr) {
        if (xmlr.hasName()) {
            String prefix = xmlr.getPrefix();
            String uri = xmlr.getNamespaceURI();
            String localName = xmlr.getLocalName();
        }
    }

    private static void processAttributes(XMLStreamReader xmlr) {
        for (int i = 0; i < xmlr.getAttributeCount(); i++)
            processAttribute(xmlr, i);
    }

    private static void processAttribute(XMLStreamReader xmlr, int index) {
        String prefix = xmlr.getAttributePrefix(index);
        String namespace = xmlr.getAttributeNamespace(index);
        String localName = xmlr.getAttributeName(index).getLocalPart();
        String value = xmlr.getAttributeValue(index);
    }
}
