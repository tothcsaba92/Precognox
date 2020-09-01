package com.example.demo;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthorService {


    public Map<String, Integer> showAllNames(String order, String name, String path) {
        Map<String, Integer> results = convertToMap(readFile(path));
        if (order.equals("frequency")) {
            results = sortByFrequency(results);
        }
        if (!name.isEmpty()) {
            results = searchByName(results, name);
        }
        return results;

    }

    public Map<String, Integer> convertToMap(List<String> names) {

        Map<String, Integer> authorsWithOccurrence = new TreeMap<>();
        for (String i : names) {
            Integer j = authorsWithOccurrence.get(i);
            authorsWithOccurrence.put(i, (j == null) ? 1 : j + 1);
        }
        return authorsWithOccurrence;
    }

    public List<String> readFile(String path){
        List<String> authors = new ArrayList<>();
        File xmlFile = new File(path);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;

        try {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            NodeList nodeList = doc.getElementsByTagName("datafield");
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node data = nodeList.item(i);
                if (data.getNodeType() == Node.ELEMENT_NODE) {
                    Element author = (Element) data;
                    String tag = author.getAttribute("tag");
                    NodeList nameList = author.getChildNodes();
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node name = nameList.item(j);
                        if (name.getNodeType() == Node.ELEMENT_NODE) {
                            Element info = (Element) name;
                            String code = info.getAttribute("code");
                            if (Integer.parseInt(tag) == 100 && code.equals("a")) {
                                authors.add(info.getTextContent());
                            }
                        }
                    }
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return authors;
    }

    public Map<String, Integer> sortByFrequency(Map<String, Integer> names) {
        return names.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

    }

    public Map<String, Integer> searchByName(Map<String, Integer> names, String name) {
        return names.entrySet().stream().filter(map -> map.getKey().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
