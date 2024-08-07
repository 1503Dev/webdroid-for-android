package tc.dedroid.util;
import java.util.Random;

public class DedroidCharacter {
    
    static public int randomInt(int min, int max){
        Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;
        return num;
    }
    static public String subStr(String str,int start, int end){
        return str.substring(start,end);
    }
    static public String subStr(String str,int start){
        return str.substring(start);
    }
    public static String strToUnicode(String str) {
        StringBuffer sb = new StringBuffer();
        char[] c = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            sb.append("\\u" + Integer.toHexString(c[i]));
        }
        return sb.toString();
    }
    public static String catStr(String[] strArray){
        String r="";
        for(int i=0;i<strArray.length;i++){
            r=r+strArray[i];
        }
        return r;
    }
    public static String catStr(String str1,String str2){
        String[] i={str1,str2};
        return catStr(i);
    }
}
