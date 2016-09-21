package com.ulong.stax;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;

/**
 * 这是一个使用stax解析和操作XML的类
 *
 * @author Ulong
 * @create 2016/9/21
 */
public class StAXParserDemo {
    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        boolean bAge = false;
        boolean bHeight = false;
        boolean bWeight = false;


        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLEventReader eventReader = factory.createXMLEventReader(new FileReader("learnStax/src/main/resources/room.xml"));

        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();
            switch (event.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    StartElement startElement = event.asStartElement();
                    String qName = startElement.getName().getLocalPart();
                    if (qName.equalsIgnoreCase("member")) {
                        System.out.println("Start Element : member");
                        Iterator<Attribute> attributes = startElement.getAttributes();
                        String name = attributes.next().getValue();
                        System.out.println("name : " + name);
                    } else if (qName.equalsIgnoreCase("age")) {
                        bAge = true;
                    } else if (qName.equalsIgnoreCase("height")) {
                        bHeight = true;
                    } else if (qName.equalsIgnoreCase("weight")) {
                        bWeight = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    Characters characters = event.asCharacters();
                    if (bAge) {
                        System.out.println("age: " + characters.getData());
                        bAge = false;
                    }
                    if (bHeight) {
                        System.out.println("height: " + characters.getData());
                        bHeight = false;
                    }
                    if (bWeight) {
                        System.out.println("weight: " + characters.getData());
                        bWeight = false;
                    }
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    EndElement endElement = event.asEndElement();
                    if (endElement.getName().getLocalPart().equalsIgnoreCase("member")) {
                        System.out.println("End Element : member");
                        System.out.println();
                    }
                    break;
            }
        }

    }
}