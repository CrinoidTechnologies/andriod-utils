package com.crinoidtechnologies.general.utils;

/**
 * Created by ${Vivek} on 10/18/2016 for MyVoice.Be careful
 */

public class TextUtils {

    public static String toUpperCase(String line) {
        int pos = 0;
        boolean capitalize = true;
        StringBuilder sb = new StringBuilder(line);
        while (pos < sb.length()) {
            if (sb.charAt(pos) == '.') {
                capitalize = true;
            } else if (capitalize && !Character.isWhitespace(sb.charAt(pos))) {
                sb.setCharAt(pos, Character.toUpperCase(sb.charAt(pos)));
                capitalize = false;
            }
            pos++;
        }
        return sb.toString();
    }

    public static String removeNonAsciiCharacters(String src, String replaceWith) {
//        return src.replaceAll("\\P{Print}", replaceWith);
        return src.replaceAll("[^\\p{ASCII}]", replaceWith);
    }

}
