import java.util.ArrayList;
import java.util.Arrays;

public class Lesson1 {

    static final float APPLE_WEIGHT = 1.0f;
    static final float ORANGE_WEIGHT = 1.5f;


    public static void main(String[] args) {

        Integer[] arr1 = {10, 20, 30, 40};
        String[] arr2 = {"1", "2", "3", "4"};
        System.out.println(Arrays.toString(arr1));
        exchangeElements(arr1, 2, 3);
        exchangeElements(arr2, 2, 3);
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));

        ArrayList<Integer> aL1 = toArrayList(arr1);
        aL1.add(100);
        ArrayList<String> aL2 = toArrayList(arr2);
        aL2.add("sss");
        System.out.println(aL2);
        System.out.println(aL1);

        Box<Apple> ba = new Box<>();
        ba.add(new Apple());
        ba.add(new Apple());

    }

    public static <E> boolean exchangeElements(E[] arr, int firstIndex, int secondIndex) {
        if (firstIndex >= arr.length || secondIndex >= arr.length)
            return false;

        E tmp = arr[firstIndex];
        arr[firstIndex] = arr[secondIndex];
        arr[secondIndex] = tmp;

        return true;
    }

    public static <E> ArrayList<E> toArrayList(E[] arr) {
        ArrayList<E> tmpArrayList = new ArrayList<>();
        for (E elem : arr) {
            tmpArrayList.add(elem);
        }
        return tmpArrayList;
    }
}
