package tools.basic;

public class StringUtil {
    public static String concat(String split, Object ... objects) {
        int i = 0;
        String result = "";
        for (; i < objects.length - 1; i++) {
            result += objects[i] + split;
        }
        result += objects[i];
        return result;
    }
}
