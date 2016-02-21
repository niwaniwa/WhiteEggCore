package com.github.niwaniwa.we.core.player.rank;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.github.niwaniwa.we.core.WhiteEggCore;

public class Rank implements ConfigurationSerializable {

    private static List<Rank> ranks = new ArrayList<>();
    private static File path = new File(WhiteEggCore.getInstance().getDataFolder() + File.separator + "ranks" + File.separator);

    public static boolean isLoad = false;

    private String prefix;
    private String rankName;
    private RankProperty property;
    private String permission;

    public Rank(String prefix, ChatColor color, String name, RankProperty property, String permission) {
        this.prefix = "§" + color.getChar() + prefix;
        this.rankName = name;
        this.property = property;
        this.setPermission(permission);
    }

    public Rank(String prefix, String name, RankProperty property, String permission) {
        this(prefix, ChatColor.WHITE, name, property, permission);
    }

    public Rank(String prefix, String name) {
        this(prefix, name, RankProperty.NORMAL, "");
    }

    public String getName() {
        return rankName;
    }

    public String getPrefix() {
        return prefix;
    }

    public RankProperty getProperty() {
        return property;
    }

    public void setProperty(RankProperty property) {
        this.property = property;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public void add() {
        ranks.add(this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("name", this.getName());
        result.put("prefix", this.getPrefix());
        result.put("permission", this.getPermission());
        result.put("property", getProperty());
        return result;
    }

    @Override
    public String toString() {
        return serialize().toString();
    }

    public void save() {
        if (!path.exists()) {
            path.mkdirs();
        }
        Transformer tf = null;
        try {
            tf = TransformerFactory.newInstance().newTransformer();
        } catch (TransformerConfigurationException | TransformerFactoryConfigurationError e) {
        }
        tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        tf.setOutputProperty(OutputKeys.INDENT, "yes");
        try {
            tf.transform(new DOMSource(createDocument()), new StreamResult(new File(path, this.getName() + ".xml")));
        } catch (TransformerException | ParserConfigurationException e) {
        }
    }

    private Document createDocument() throws ParserConfigurationException {
        Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        Element root = document.createElement("whiteegg");
        root.setAttribute("version", WhiteEggCore.getInstance().getDescription().getVersion());
        document.appendChild(root);

        Element rank = document.createElement("rank");
        root.appendChild(rank);

        Element name = document.createElement("name");
        Element prefix = document.createElement("prefix");
        Element permission = document.createElement("permission");
        Element property = document.createElement("property");

        name.appendChild(document.createTextNode(this.getName()));
        prefix.appendChild(document.createTextNode(this.getPrefix()));
        permission.appendChild(document.createTextNode(this.getPermission()));
        property.appendChild(document.createTextNode(this.getProperty().toString()));
        rank.appendChild(name);
        rank.appendChild(prefix);
        rank.appendChild(permission);
        rank.appendChild(property);

        return document;
    }

    public static List<Rank> getRanks() {
        return ranks;
    }

    public static Rank parserRank(Map<String, Object> o) {
        String prefix = String.valueOf(o.get("prefix"));
        String name = String.valueOf(o.get("name"));
        RankProperty property = RankProperty.valueOfString(String.valueOf(o.get("property")));
        String permission = String.valueOf(o.get("permission"));
        if (prefix == null
                || name == null
                || property == null
                || permission == null) {
            return null;
        }
        Rank rank = new Rank(prefix, name, property, permission);
        return rank;
    }

    public static boolean check(Rank rank) {
        if (rank == null) {
            return false;
        }
        for (Rank r : ranks) {
            if (r.equals(rank)) {
                return true;
            }
        }
        return false;
    }

    public static void saveAll() {
        for (Rank r : ranks) {
            r.save();
        }
    }

    public static void load() {
        if (!path.exists()) {
            isLoad = true;
            return;
        }
        Arrays.asList(path.listFiles()).forEach(f -> load(f));
        isLoad = true;
    }

    private static void load(File f) {
        if (!f.exists()
                || f.getName().endsWith(".xml")) {
            return;
        }
        Document document = null;
        try {
            document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(f);
        } catch (SAXException | IOException | ParserConfigurationException e) {
        }
        Element root = document.getDocumentElement();
        Node rankNode = root.getChildNodes().item(0);
        Map<String, Object> result = new HashMap<>();
        if (rankNode.getNodeType() == Node.ELEMENT_NODE) {
            Element rankElement = (Element) rankNode;
            for (int i = 0; i < rankElement.getChildNodes().getLength(); i++) {
                Node type = rankElement.getChildNodes().item(i);
                result.put(type.getNodeName(), type.getTextContent());
            }
        }
        Rank r = parserRank(result);
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(r.getName())) {
                return;
            }
        }
        r.add();
    }

}
