import java.util.*;
import java.util.Arrays;

public class CollectionsHomework {

    public static <T> Set<T> getUniqueElements(ArrayList<T> list) {
        return new HashSet<>(list);
    }

    public static boolean isAnagram(String s, String t) {
        if (s.length() != t.length()) {
            return false;
        }

        char[] sArray = s.toLowerCase().toCharArray();
        char[] tArray = t.toLowerCase().toCharArray();

        Arrays.sort(sArray);
        Arrays.sort(tArray);

        return Arrays.equals(sArray, tArray);
    }

    static class PowerfulSet {

        // Пересечение двух наборов
        public <T> Set<T> intersection(Set<T> set1, Set<T> set2) {
            Set<T> result = new HashSet<>(set1);
            result.retainAll(set2);
            return result;
        }

        public <T> Set<T> union(Set<T> set1, Set<T> set2) {
            Set<T> result = new HashSet<>(set1);
            result.addAll(set2);
            return result;
        }

        public <T> Set<T> relativeComplement(Set<T> set1, Set<T> set2) {
            Set<T> result = new HashSet<>(set1);
            result.removeAll(set2);
            return result;
        }
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

        System.out.println("\n=== Задание 2: Проверка анаграмм ===");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Введите первую строку: ");
        String s = scanner.nextLine();

        System.out.print("Введите вторую строку: ");
        String t = scanner.nextLine();

        boolean result = isAnagram(s, t);
        System.out.println("Результат: " + result);

        System.out.println("\nТестирование примеров из задания:");
        String[][] testCases = {
                {"Бейсбол", "бобслей"},
                {"Героин", "регион"},
                {"Клоака", "околка"},
                {"test", "tset"},
                {"hello", "world"}
        };

        for (String[] testCase : testCases) {
            boolean isAnagram = isAnagram(testCase[0], testCase[1]);
            System.out.printf("\"%s\" и \"%s\" -> %s%n",
                    testCase[0], testCase[1], isAnagram);
        }

        System.out.println("\n=== Задание 3: PowerfulSet ===");

        PowerfulSet powerfulSet = new PowerfulSet();

        Set<Integer> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        set1.add(3);

        Set<Integer> set2 = new HashSet<>();
        set2.add(0);
        set2.add(1);
        set2.add(2);
        set2.add(4);

        System.out.println("set1: " + set1);
        System.out.println("set2: " + set2);

        Set<Integer> intersection = powerfulSet.intersection(set1, set2);
        System.out.println("Пересечение (intersection): " + intersection);

        Set<Integer> union = powerfulSet.union(set1, set2);
        System.out.println("Объединение (union): " + union);

        Set<Integer> complement = powerfulSet.relativeComplement(set1, set2);
        System.out.println("Относительное дополнение (relativeComplement): " + complement);

        scanner.close();
    }
}
