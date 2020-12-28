import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.net.URL;
import java.net.URLConnection;
import java.io.PrintStream;

public class test1 {
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
    //    public static void outputFile(String outputfile)throws IOException {
//        PrintStream ps = new PrintStream("Test.txt");
//        System.setOut(ps);
//        System.out.println(outputfile);
//        System.out.println();
//        System.out.println();
//        ps.close();
//    }
    public static void script(String resources)throws IOException{
        String strUrl = "http://www.myexperiment.org/"+resources+".xml?id=";
        String path =resources;
        File file = new File("."+File.separator+path);
        if(!file.exists()){
            file.mkdir();
        }
        try {
            for (int i = 1; i <= 5; i++) {
                String input = loadAddrsApi(strUrl, i);
                if(!input.equals("")){
                    BufferedWriter bw = new BufferedWriter(new FileWriter("."+File.separator+path+File.separator+i + ".txt"));
                    bw.write(input);
                    bw.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void test(){
        String path = "./test";
        File file = new File(path);
        if(!file.exists())
            file.mkdir();
    }
    public static void main(String[] args)throws IOException{
        //String strUrl = "http://www.myexperiment.org/user.xml?id=";
        //test();
        script("workflow");
        script("user");
    }
}
