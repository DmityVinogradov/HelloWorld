import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class HomeworkCollections {

    public static <T> Set<T> getUniqueElements(ArrayList<T> list) {
        return new HashSet<>(list);
    }

    public static void main(String[] args) {
        System.out.println("=== Задание 1: Уникальные элементы ===");

        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(2);
        numbers.add(1);
        numbers.add(4);

        System.out.println("Исходный список: " + numbers);
        System.out.println("Уникальные элементы: " + getUniqueElements(numbers));

        ArrayList<String> strings = new ArrayList<>();
        strings.add("apple");
        strings.add("banana");
        strings.add("apple");
        strings.add("orange");
        strings.add("banana");

        System.out.println("Исходный список строк: " + strings);
        System.out.println("Уникальные строки: " + getUniqueElements(strings));
    }
}
