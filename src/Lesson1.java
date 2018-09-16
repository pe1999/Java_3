import java.util.ArrayList;
import java.util.Arrays;

public class Lesson1 {
    public static void main(String[] args) {

        Integer[] arr1 = {10, 20, 30, 40};
        String[] arr2 = {"1", "2", "3", "4"};
        System.out.println(Arrays.toString(arr1));
        exchangeElements(arr1, 2, 3);
        exchangeElements(arr2, 2, 3);
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));

    }

    public static <E> boolean exchangeElements(E[] arr, int firstIndex, int secondIndex) {
        if (firstIndex >= arr.length || secondIndex >= arr.length)
            return false;

        E tmp = arr[firstIndex];
        arr[firstIndex] = arr[secondIndex];
        arr[secondIndex] = tmp;

        return true;
    }

    public static <E> boolean toArrayList(E[] arr, ArrayList<E> arrayList) {
        ArrayList<E> tmp = new ArrayList<E>();
        arrayList.clear();
        for (E elem : arr) {
            arrayList.add(elem);
        }
        return true;
    }
}
