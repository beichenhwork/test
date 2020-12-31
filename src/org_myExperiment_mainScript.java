import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.net.URL;
import java.net.URLConnection;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.*;

public class org_myExperiment_mainScript {
    public static String loadAddrsApi(String strUrl,int id) {
        StringBuffer sb;
        //String strUrl = "http://www.myexperiment.org/user.xml?id=";
        sb = new StringBuffer();
        try {
            String curUrl = strUrl + id;
            URL url = new URL(curUrl);
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            sb = new StringBuffer();
        }
        return sb.toString();
    }

    public static String loadSourceXML(String strUrl) {
        StringBuffer sb;
        //String strUrl = "http://www.myexperiment.org/user.xml?id=";
        sb = new StringBuffer();
        try {
            String curUrl = strUrl;
            URL url = new URL(curUrl);
            URLConnection con = url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            sb = new StringBuffer();
        }
        return sb.toString();
    }

    public static void org_myExperiment_getMetaData(String resources)throws IOException{
        String strUrl = "http://www.myexperiment.org/"+resources+".xml?id=";
        String path = "";
        if(resources.equals("user"))
            path = "Users_metaData";
        if(resources.equals("workflow"))
            path = "Workflows_metaData";

        File file = new File("."+File.separator+path);
        if(!file.exists()){
            file.mkdir();
        }
        try {
            for (int i = 1; i <= 6000; i++) {
                String input = loadAddrsApi(strUrl, i);
                if(!input.equals("")){
                    String curPath = "."+File.separator+path+File.separator+i + ".xml";
                    BufferedWriter bw = new BufferedWriter(new FileWriter(curPath));
                    bw.write(input);
                    bw.close();
                    readXML(curPath);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void org_myExperiment_getSourceData(String resources, String content_url, int id)throws IOException {
        String path = "";
        if (resources.equals("user"))
            path = "Users_sourceData";
        if (resources.equals("workflow"))
            path = "Workflows_sourceData";

        File file = new File("." + File.separator + path);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            String input = loadSourceXML(content_url);
            if (!input.equals("")) {
                BufferedWriter bw = new BufferedWriter(new FileWriter("." + File.separator + path + File.separator + id + ".xml"));
                bw.write(input);
                bw.close();
                org_myExperiment_getworkflowAPIs(regExpression(input),id);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void org_myExperiment_getworkflowAPIs(ArrayList<String> findAPI,int id)throws IOException{
        String path = "Workflows_APIs";
        File file = new File("." + File.separator + path);
        if (!file.exists()) {
            file.mkdir();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter("." + File.separator+path+File.separator +id+".txt"));
        for(String cur: findAPI){
            bw.write(cur);
            bw.write("\n");

        }
        bw.close();
    }
    public static void readXML(String path){
        try {
            File fXmlFile = new File(path);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            //optional, but recommended
            //read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            doc.getDocumentElement().normalize();

            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName(doc.getDocumentElement().getNodeName());

            System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {

                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    if(nNode.getNodeName().equals("workflow")){
                        System.out.println("Workflow id : " + eElement.getAttribute("id"));
                        System.out.println("Title : " + eElement.getElementsByTagName("title").item(0).getTextContent());
                        System.out.println("Created Time : " + eElement.getElementsByTagName("created-at").item(0).getTextContent());
                        System.out.println("Preview : " + eElement.getElementsByTagName("preview").item(0).getTextContent());
                        System.out.println("Content-url : " + eElement.getElementsByTagName("content-uri").item(0).getTextContent());
                        org_myExperiment_getSourceData(doc.getDocumentElement().getNodeName(), eElement.getElementsByTagName("content-uri").item(0).getTextContent(),Integer.parseInt(eElement.getAttribute("id")));
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static ArrayList<String> regExpression(String sourceXML){
        ArrayList<String> findAPI = new ArrayList<>();
        // regex format, it matches all http or https url
        Pattern p = Pattern.compile("(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        // match the regex
        Matcher m = p.matcher(sourceXML);
        while(m.find()){
            System.out.println(m.group());
            findAPI.add(m.group());
        }
        return findAPI;
    }
    public static void main(String[] args)throws IOException{
        //String strUrl = "http://www.myexperiment.org/user.xml?id=";
        //test();
        org_myExperiment_getMetaData("workflow");

    }
}
