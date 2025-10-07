import java.util.Arrays;
import java.util.Scanner;

public class AnagramChecker {

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

    public static void main(String[] args) {
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

        scanner.close();
    }
}
