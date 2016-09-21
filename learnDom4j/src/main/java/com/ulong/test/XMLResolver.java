package com.ulong.test;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.Iterator;

/**
 * 这是一个使用dom4j解析和操作XML的类
 *
 * @author Ulong
 * @create 2016/9/20
 */
public class XMLResolver {
    public static void main(String[] args) throws DocumentException {
        //读取XML文件，获取document对象
        SAXReader saxReader = new SAXReader();
        File xmlFile = new File("learnDom4j/src/main/resources/files/movie.xml");
        System.out.println(xmlFile.getAbsolutePath());
        Document document = saxReader.read(xmlFile);

        //字符串转xml
        String xml = "<IDE><idea name=\"intellij idea\"></idea></IDE>";
        Document domFromString = DocumentHelper.parseText(xml);

        //直接创建xml dom
        Document newDom = DocumentHelper.createDocument();
        Element newRoot = newDom.addElement("root");
        newRoot.addAttribute("name","root");

        //获取文档根节点
        Element root = document.getRootElement();
        //获取某节点指定名称的子节点
        Element movie = root.element("movie");
        Attribute attribute = movie.attribute("name");
        //获取某节点的文本
        String s = attribute.getText();
        System.out.println(s);

        //遍历某节点的子节点
        for(Iterator it = movie.elementIterator(); it.hasNext();){
            Element ele = ((Element) it.next());
            System.out.println("tag: "+ele.getName()+"\tpath: "+ele.getPath()+"\tnamespace: "+ ele.getNamespace());
        }

        //遍历名为movie的子节点
        for(Iterator it = root.elementIterator("movie"); it.hasNext();){
            Element ele = ((Element) it.next());
            System.out.println("tag: "+ele.getName()+"\tname: "+ele.attribute("name").getText()+"\ttype:"+ele.element("type").getText());
        }

        //遍历某节点的属性
        for (Iterator it = movie.attributeIterator(); it.hasNext();) {
            Attribute attr = (Attribute) it.next();
            String text = attr.getText();
            System.out.println(text);
        }

        treeWalk(root);
    }

    public static void treeWalk(Element ele){
        int count = 0;
        for(Iterator it = ele.elementIterator();it.hasNext();){
            Element element = ((Element) it.next());
            System.out.println(element.getName());
            treeWalk(element);
        }
    }
}
