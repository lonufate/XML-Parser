package com.ulong.stax;

import javax.xml.stream.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashSet;

/**
 * 这是一个校验XML复杂度的类
 *
 * @author Ulong
 * @create 2016/9/23
 */
public class XMLCheck {
    private static final int maxNodeNum = 30;
    private static final int maxAttrNum = 10;
    private static final int maxNamespaceNum = 6;
    private static final int maxDepth = 8;
    private static final int maxLeafAttrNum = 5;
    private static final int maxLeafNamespaceNum = 3;
    private static final int maxNodeText = 50;
    private static final int maxAttrText = 30;
    private static final int maxNamespaceLength = 80;
    private static final int maxComment = 100;

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException {
        int nodeNum = 0;
        int attrNum = 0;
        HashSet<String> namespaces = new HashSet<>();
        int depth = 0;
        int leafAttrNum = 0;
        int leafNamespaceNum = 0;
        int nodeTextLength = 0;
        int attrTextLength = 0;
        int namespaceLength = 0;
        int commentLength = 0;
        boolean isLeaf = false;

        FileReader fr = new FileReader(new File("learnStax/src/main/resources/room.xml"));
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader sr = factory.createXMLStreamReader(fr);
        XMLStreamReader fsr = factory.createFilteredReader(sr, filter);


        while (fsr.hasNext()) {
            switch (fsr.getEventType()) {
                case XMLStreamConstants.START_ELEMENT:
                    if (nodeNum < maxNodeNum) {
                        nodeNum++;
                    } else {
                        warn("节点总数超限", fsr);
                        return;
                    }
                    if (attrNum < maxAttrNum) {
                        leafAttrNum = fsr.getAttributeCount();
                        for (int i = 0; i < leafAttrNum; i++) {
                            if (fsr.getAttributeValue(i).length() > maxAttrText) {
                                warn("属性文本长度超限！", fsr);
                                return;
                            }
                        }
                        attrNum += leafAttrNum;
                    } else {
                        warn("属性总数超限！", fsr);
                        return;
                    }
                    if (namespaces.size() < maxNamespaceNum) {
                        leafNamespaceNum = fsr.getNamespaceCount();
                        for (int i = 0; i < leafNamespaceNum; i++) {
                            if (fsr.getNamespaceURI(i).length() > maxNamespaceLength) {
                                warn("命名空间文本长度超限！", fsr);
                            }
                            namespaces.add(fsr.getNamespaceURI(i));
                        }
                    } else {
                        warn("命名空间总数超限！", fsr);
                        return;
                    }
                    if (depth < maxDepth) {
                        depth++;
                    } else {
                        warn("节点深度超限！", fsr);
                        return;
                    }
                    isLeaf = true;
                    break;
                case XMLStreamConstants.END_ELEMENT:
                    if (isLeaf) {
                        if (leafAttrNum > maxLeafAttrNum) {
                            warn("叶节点属性个数超限！", fsr);
                            return;
                        }
                        if (leafNamespaceNum > maxLeafNamespaceNum) {
                            warn("叶节点命名空间个数超限！", fsr);
                            return;
                        }
                        depth--;
                        isLeaf = false;
                    }
                    nodeTextLength = 0;
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if (fsr.hasText()) {
                        nodeTextLength += fsr.getText().length();
                        if (nodeTextLength > maxNodeText) {
                            warn("节点文本长度超限！", fsr);
                            return;
                        }
                    }
                    break;
                case XMLStreamConstants.COMMENT:
                    if (fsr.hasText()) {
                        if (fsr.getText().length() > maxComment) {
                            warn("注释文本长度超限！", fsr);
                            return;
                        }
                    }
                    break;
                case XMLStreamConstants.ENTITY_REFERENCE:
                    System.out.println("[INFO]there is EntityReference");
                    break;
            }
            fsr.next();
        }
    }

    //过滤出我们需要检查的东西
    private static StreamFilter filter = new StreamFilter() {
        @Override
        public boolean accept(XMLStreamReader reader) {
            switch (reader.getEventType()) {    //实体的eventType也是Character
                case XMLStreamConstants.START_ELEMENT:
                case XMLStreamConstants.END_ELEMENT:
                case XMLStreamConstants.CHARACTERS:
                case XMLStreamConstants.COMMENT:
                case XMLStreamConstants.ENTITY_REFERENCE:
                    return true;
                default:
                    return false;
            }
        }
    };

    private static void warn(String s, XMLStreamReader fsr) {
        Location loc = fsr.getLocation();
        int line = loc.getLineNumber();
        int column = loc.getColumnNumber();
        System.out.println("[WARN]" + s + ":\n\tin line:" + line + "    column:" + column);
        switch (fsr.getEventType()) {
            case XMLStreamConstants.START_ELEMENT:
            case XMLStreamConstants.END_ELEMENT:
                System.out.println("\t<" + fsr.getLocalName() + ">");
                break;
            case XMLStreamConstants.CHARACTERS:
            case XMLStreamConstants.COMMENT:
                if (fsr.hasText()) {
                    System.out.println("\t"+fsr.getText());
                }
                break;
        }
    }
}
