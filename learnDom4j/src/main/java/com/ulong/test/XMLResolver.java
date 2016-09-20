package com.ulong.test;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;

/**
 * 这是一个解析和操作XML的类
 *
 * @author Ulong
 * @create 2016/9/20
 */
public class XMLResolver {
    public static void main(String[] args) {
        //读取XML文件，获取document对象
        SAXReader saxReader = new SAXReader();
        try {
            File xmlFile = new File("pom.xml")/*new File("learnDom4j/src/main/resources/files/movie.xml")*/;
            System.out.println(xmlFile.getAbsolutePath());
            Document document = saxReader.read(xmlFile);

            //获取文档根节点
            Element root = document.getRootElement();
            //获取某节点指定名称的子节点
            Element artifactId = root.element("artifactId");
            //获取某节点的文本
            String s = artifactId.getText();
            System.out.println(s);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
