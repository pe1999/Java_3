package lesson1;

import java.util.ArrayList;
import java.util.Arrays;

public class Lesson1 {

    // Константы для классов фруктов и коробки с фруктами.
    static final float APPLE_WEIGHT = 1.0f;
    static final float ORANGE_WEIGHT = 1.5f;
    static final float WEIGHT_DELTA = 0.001f;

    public static void main(String[] args) {

        // Демонтрация работы задания 1
        System.out.println("\n\nЗадание 1\n");
        System.out.println("Создаем массив 1 типа Integer.");
        System.out.println("Создаем массив 2 типа String.");

        Integer[] arr1 = {10, 20, 30, 40};
        String[] arr2 = {"Строка 1", "Строка 2", "Строка 3", "Строка 4"};

        System.out.println("Массив 1: " + Arrays.toString(arr1));
        System.out.println("Массив 2: " + Arrays.toString(arr2));

        System.out.println("Мнеяем местами в массиве 1 элементы с индексами 2 и 3.");
        System.out.println("Мнеяем местами в массиве 2 элементы с индексами 0 и 3.");
        exchangeElements(arr1, 2, 3);
        exchangeElements(arr2, 0, 3);

        System.out.println("Массив 1: " + Arrays.toString(arr1));
        System.out.println("Массив 2: " + Arrays.toString(arr2));

        // Демонтрация работы задания 2
        System.out.println("\n\nЗадание 2\n");
        System.out.println("Используем массивы из задания 1.");
        System.out.println("Преобразовываем массив 1 в ArrayList 1, добавляем в него еще один элемент: 100.");
        ArrayList<Integer> aL1 = toArrayList(arr1);
        aL1.add(100);
        System.out.println("Преобразовываем массив 2 в ArrayList 2, добавляем в него еще один элемент: \"Строка 10\".");
        ArrayList<String> aL2 = toArrayList(arr2);
        aL2.add("Строка 10");
        System.out.println("ArrayList 1: " + aL1);
        System.out.println("ArrayList 2: " + aL2);

        // Демонтрация работы задания 3
        System.out.println("\n\nЗадание 3\n");
        System.out.println("Создаем две коробки для яблок и добавляем в 1 коробку 3 яблока");
        System.out.println("Создаем одну коробку для апельсинов и добавляем в нее 2 апельсина");
        Box<Apple> appleBox1 = new Box<>();
        Box<Apple> appleBox2 = new Box<>();
        Box<Orange> orangeBox1 = new Box<>();

        appleBox1.add(new Apple());
        appleBox1.add(new Apple());
        appleBox1.add(new Apple());

        orangeBox1.add(new Orange());
        orangeBox1.add(new Orange());

        System.out.println("Сравнение веса коробок с 3 яблоками и с 2 апельсинами: " + appleBox1.compare(orangeBox1));

        System.out.println("Вес коробки с яблоками 1 до пересыпания: " + appleBox1.getWeight());
        System.out.println("Вес коробки с яблоками 2 до пересыпания: " + appleBox2.getWeight());
        System.out.println("Пересыпаем коробку 1 в коробку 2.");
        appleBox1.reloadTo(appleBox2);
        System.out.println("Вес коробки с яблоками 1 после пересыпания: " + appleBox1.getWeight());
        System.out.println("Вес коробки с яблоками 2 после пересыпания: " + appleBox2.getWeight());
    }


    /**
     * Задание 1
     * Метод меняет местами элементы массива arr с индексами firstIndex и secondIndex
     * Возвращает true - если обмен произошел, false - если индексы выходят за пределы
     * массива.
     */
    public static <E> boolean exchangeElements(E[] arr, int firstIndex, int secondIndex) {
        if (firstIndex >= arr.length || secondIndex >= arr.length)
            return false;

        E tmp = arr[firstIndex];
        arr[firstIndex] = arr[secondIndex];
        arr[secondIndex] = tmp;

        return true;
    }

    /**
     * Задание 2
     * Метод получает массив arr и возвращает ArrayList, в который скопированы элементы
     * массива.
     */
    public static <E> ArrayList<E> toArrayList(E[] arr) {
        ArrayList<E> tmpArrayList = new ArrayList<>();
        for (E elem : arr) {
            tmpArrayList.add(elem);
        }
        return tmpArrayList;
    }
}
