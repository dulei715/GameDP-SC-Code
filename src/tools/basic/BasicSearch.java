package tools.basic;

public class BasicSearch {
    public static final boolean FORMER = false;
    public static final boolean LATTER = true;
    public static int binarySearch(Comparable[] arr, Comparable element, Boolean border) {
        int len = arr.length;
        int from = 0, end = len - 1;
        int mid;
        int tempCompare;
        while (from < end) {
            mid = (from + end) / 2;
//            tempCompare = arr[mid].compareTo(element);
            tempCompare = element.compareTo(arr[mid]);
            if (tempCompare == 0) {
                return mid;
            }
            if (tempCompare < 0) {
                end = mid -1 ;
            } else {
                from = mid + 1;
            }
        }
        if (border == null) {
            return -2;
        }
        if (border.equals(BasicSearch.FORMER)) {
            return from - 1;
        }
        return from;
    }
}
