package com.ulong.stax;

import javax.xml.namespace.QName;
import javax.xml.stream.*;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

/**
 * 这是一个使用stax解析和操作XML的类
 *
 * @author Ulong
 * @create 2016/9/21
 */
public class StAXParserDemo {
    private static HashSet<String> namespaces = new HashSet<>();

    public static void main(String[] args) throws IOException, XMLStreamException {
        boolean bAge = false;
        boolean bHeight = false;
        boolean bWeight = false;

        //1st step: get inputFactor
        XMLInputFactory factory = XMLInputFactory.newInstance();

        FileReader fileReader = new FileReader("learnStax/src/main/resources/room.xml");
        /*//2nd step: create eventReader
        XMLEventReader eventReader = factory.createXMLEventReader(fileReader);

        //打印所有类型的event
        //treeWalk(eventReader);

        //begin stax事件过滤器 打印过滤后的event
        //return (!event.isCharacters());
        XMLEventReader nodeReader = factory.createFilteredReader(eventReader, new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return event.isStartElement();
            }
        });
        int nodeNum = treeWalk(nodeReader);
        System.out.println("节点总数：" + nodeNum);

        //stream会被关掉，得新建一个
        fileReader = new FileReader("learnStax/src/main/resources/room.xml");
        XMLEventReader eventReader2 = factory.createXMLEventReader(fileReader);
        XMLEventReader attrReader = factory.createFilteredReader(eventReader2, new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return event.isAttribute();
            }
        });
        int attrNum = getAttrNum(attrReader);
        System.out.println("属性总数：" + attrNum);


        String xml = "<member name=\"Nero\"><age>18</age><height>172cm</height><weight>60kg</weight></member>";
        //BufferedReader bf = new BufferedReader(new StringReader(xml));

        XMLEventReader eventReader3 = factory.createXMLEventReader(new StringReader(xml));
        XMLEventReader nodeReader3 = factory.createFilteredReader(eventReader3, new EventFilter() {
            @Override
            public boolean accept(XMLEvent event) {
                return event.isStartElement();
            }
        });
        int nodeNum3 = treeWalk(nodeReader3);
        System.out.println("节点总数：" + nodeNum3);
        //end*/

        //begin 使用stax流过滤器
        fileReader = new FileReader("learnStax/src/main/resources/room.xml");
        XMLStreamReader xmlSR = factory.createXMLStreamReader(fileReader);
        //XMLStreamReader xmlFSR = factory.createFilteredReader(xmlSR, filter);
        XMLStreamReader xmlFSR = factory.createFilteredReader(xmlSR, tagFilter);
        //treeWalk(xmlFSR);

        //可以实现嵌套过滤！！！  过滤器依次执行
        treeWalk(factory.createFilteredReader(xmlFSR,leafFilter));
        //end

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
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            System.out.println(event);
            count++;
        }
        return count;
    }

    private static void treeWalk(XMLStreamReader sr) throws XMLStreamException {
        int nodeNum = 0;
        int attrNum = 0;
        while (sr.hasNext()) {
            switch (sr.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    processName(sr);
                    processAttributes(sr);
                    processNamespace(sr);
                    attrNum += sr.getAttributeCount();
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    nodeNum++;
                    processName(sr);
                    break;
                case XMLStreamConstants.SPACE:
                case XMLStreamConstants.CHARACTERS:
                    System.out.println(sr.getText());
                    break;
                case XMLStreamConstants.COMMENT:
                case XMLStreamConstants.PROCESSING_INSTRUCTION:
                    if (sr.hasText()) {
                        System.out.println("<--" + sr.getText() + "-->");
                    }
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    break;
            }
            sr.next();
        }
        System.out.println("节点总数：" + nodeNum);
        System.out.println("属性总数：" + attrNum);
        System.out.println("命名空间总数：" + namespaces.size());
    }

    private static void processNamespace(XMLStreamReader sr) {
        for (int i = 0; i < sr.getNamespaceCount(); i++) {
            String namespaceURI = sr.getNamespaceURI(i);
            System.out.println(namespaceURI);
            namespaces.add(namespaceURI);
        }
    }

    private static void processAttributes(XMLStreamReader sr) {
        for (int i = 0; i < sr.getAttributeCount(); i++) {
            String prefix = sr.getAttributePrefix(i);
            String namespace = sr.getAttributeNamespace(i);
            String name = sr.getAttributeLocalName(i);
            String value = sr.getAttributeValue(i);
        }
    }

    private static void processName(XMLStreamReader sr) throws XMLStreamException {
        if (sr.hasName()) {
            String prefix = sr.getPrefix();
            String namespace = sr.getNamespaceURI();
            String name = sr.getLocalName();
            if (sr.getEventType() == XMLStreamConstants.END_ELEMENT) {
                System.out.println("</" + name + ">");
                return;
            }
            System.out.println("<" + name + ">");
        }
    }

    private static int getAttrNum(XMLEventReader reader) throws XMLStreamException {
        int count = 0;
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            StartElement ele = event.asStartElement();
            for (Iterator it = ele.getAttributes(); it.hasNext(); ) {
                Attribute attr = ((Attribute) it.next());
                System.out.println(attr.getName() + "= \"" + attr.getValue() + "\"");
                count++;
            }
        }
        return count;
    }


    // Exclusion path
    private static QName[] exclude = new QName[]{
            new QName("rooms"), new QName("script")};
    /**
     * Stax流过滤器实例
     */
    private static StreamFilter filter = new StreamFilter() {
        // Element level
        int depth = -1;
        // Last matching path segment
        int match = -1;
        // Filter result
        boolean process = true;
        // Character position in document
        int currentPos = -1;

        /**
         * 有资料说hasNext() next() 和peek()都会调用accept()
         *但是据我调试来看 hasNext() 并不会调用accept()
         */
        @Override
        public boolean accept(XMLStreamReader reader) {
            // Get character position
            Location loc = reader.getLocation();
            int pos = loc.getCharacterOffset();
            // Inhibit double execution
            if (pos != currentPos) {
                currentPos = pos;
                switch (reader.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        // Increment element depth
                        if (++depth < exclude.length && match == depth - 1) {
                            // Compare path segment with current element
                            if (reader.getName().equals(exclude[depth]))
                                // Equal - set segment pointer
                                match = depth;
                        }
                        // Process all elements not in path
                        process = match < exclude.length - 1;
                        break;
                    // End of XML element
                    case XMLStreamConstants.END_ELEMENT:
                        // Process all elements not in path
                        process = match < exclude.length - 1;
                        // Decrement element depth
                        if (--depth < match)
                            // Update segment pointer
                            match = depth;
                        break;
                }
            }
            return process;
        }
    };

    /**
     * 过滤出起止标签的过滤器
     */
    private static StreamFilter tagFilter = new StreamFilter() {
        boolean process = false;

        @Override
        public boolean accept(XMLStreamReader reader) {
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    process = true;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    process = true;
                    break;
                default:
                    process = false;
                    break;
            }
            return process;
        }
    };

    /**
     * 过滤出叶子节点的过滤器
     */
    private static StreamFilter leafFilter = new StreamFilter() {
        boolean process = false;
        String start;
        boolean isLeaf = true;

        @Override
        public boolean accept(XMLStreamReader reader) {
            switch (reader.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    start = "<"+reader.getLocalName()+">";
                    isLeaf = true;
                    process = false;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if(isLeaf){
                        process = true;
                        isLeaf = false;
                        System.out.println(start);
                    }else {
                        process = false;
                    }

                    break;
                default:
                    process = false;
            }
            return process;
        }
    };
}