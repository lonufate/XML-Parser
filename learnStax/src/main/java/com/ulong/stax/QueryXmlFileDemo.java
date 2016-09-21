package com.ulong.stax;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class QueryXmlFileDemo {
    public static void main(String[] args) throws DocumentException,IOException {
        File inputFile = new File("input.txt");

        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(inputFile);

        System.out.println("Root element :"
                + document.getRootElement().getName());

        Element classElement = document.getRootElement();

        List<Element> supercarList = classElement.elements("supercars");
        System.out.println("----------------------------");

        for (int temp = 0; temp < supercarList.size(); temp++) {
            Element supercarElement = supercarList.get(temp);
            System.out.println("\nCurrent Element :"
                    + supercarElement.getName());
            Attribute attribute = supercarElement.attribute("company");
            System.out.println("company : "
                    + attribute.getValue());
            List<Element> carNameList = supercarElement.elements("carname");
            for (int count = 0;
                 count < carNameList.size(); count++) {
                Element carElement = carNameList.get(count);
                System.out.print("car name : ");
                System.out.println(carElement.getText());
                System.out.print("car type : ");
                Attribute typeAttribute = carElement.attribute("type");
                if (typeAttribute != null)
                    System.out.println(typeAttribute.getValue());
                else {
                    System.out.println("");
                }
            }
        }
    }
}