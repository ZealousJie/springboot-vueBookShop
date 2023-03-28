package com.example.demo.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.tree.DefaultProcessingInstruction;

import javax.xml.bind.Element;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ author zealousJie
 * @ version 1.0
 */
@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class XmlNode {

    private Map<String,String> attribute = new HashMap<>();

    private List<XmlNode> nodes = new ArrayList<>();

    private Element element;

    private String name;

    private String text;

    private Map<Integer,String> comments = new HashMap<>();

    private XmlNode parentNode;

    private List<DefaultProcessingInstruction> processingInstructions;


    public static XmlNode newInstance(){
        return new XmlNode();
    }
}
