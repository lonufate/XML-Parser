package com.ulong.stax;

import javax.xml.bind.Element;
import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.util.Iterator;

/**
 * 这是一个使用stax解析和操作XML的类
 *
 * @author Ulong
 * @create 2016/9/21
 */
public class StAXParserDemo {
    public static void main(String[] args) throws IOException, XMLStreamException {
        boolean bAge = false;
        boolean bHeight = false;
        boolean bWeight = false;

        //1st step: get inputFactor
        XMLInputFactory factory = XMLInputFactory.newInstance();
        FileReader fileReader = new FileReader("learnStax/src/main/resources/room.xml");
        //2nd step: create eventReader
        XMLEventReader eventReader = factory.createXMLEventReader(fileReader);

        //打印所有类型的event
        //treeWalk(eventReader);

        //begin 打印过滤后的event
        //return (!event.isCharacters());
        XMLEventReader nodeReader = factory.createFilteredReader(eventReader, XMLEvent::isStartElement);
        int nodeNum = treeWalk(nodeReader);
        System.out.println("节点总数："+nodeNum);

        //stream会被关掉，得新建一个
        fileReader = new FileReader("learnStax/src/main/resources/room.xml");
        XMLEventReader eventReader2 = factory.createXMLEventReader(fileReader);
        XMLEventReader attrReader = factory.createFilteredReader(eventReader2, XMLEvent::isStartElement);
        int attrNum = getAttrNum(attrReader);
        System.out.println("属性总数："+attrNum);
        //end

        String xml = "<member name=\"Nero\"><age>18</age><height>172cm</height><weight>60kg</weight></member>";
        //BufferedReader bf = new BufferedReader(new StringReader(xml));

        XMLEventReader eventReader3 = factory.createXMLEventReader(new StringReader(xml));
        XMLEventReader nodeReader3 = factory.createFilteredReader(eventReader3, XMLEvent::isStartElement);
        int nodeNum3 = treeWalk(nodeReader3);
        System.out.println("节点总数："+nodeNum3);
        /*while (eventReader.hasNext()) {
            //eventReader逐行读取每一个标签，还包括\n
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
        }*/

    }

    private static int treeWalk(XMLEventReader reader) throws XMLStreamException {
        int count = 0;
        while(reader.hasNext()){
            XMLEvent event = reader.nextEvent();
            System.out.println(event);
            count++;
        }
        return count;
    }

    private static int getAttrNum(XMLEventReader reader) throws XMLStreamException {
        int count = 0;
        while (reader.hasNext()){
            XMLEvent event = reader.nextEvent();
            StartElement ele = event.asStartElement();
            for(Iterator it = ele.getAttributes();it.hasNext();){
                Attribute attr = ((Attribute) it.next());
                System.out.println(attr.getName()+"= \""+attr.getValue()+"\"");
                count++;
            }
        }
        return count;
    }
}