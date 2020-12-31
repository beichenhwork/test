package Regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 1.the class of regex is Pattern but not in Rxgex，all method we used are in Pattern
 * 2.case 1: judge abcdef
 */
public class regexTest {
    public static void main(String[] args) {

        String str = "abcdef";

        // System.out.println(str.equals("abc"));

        /**
         * judge regex
         * 1.compile(String regex)
         * 2.matcher(CharSequence input)
         * 3.matches()
         */
        // compile in regex
        Pattern p = Pattern.compile("abc");
        // match regex
        Matcher m = p.matcher(str);
        // show the matchtes
        while(m.find())
            System.out.println(m.group());

        // judge
        boolean b = m.matches();
        System.out.println(b);

    }
}