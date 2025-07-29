package HW_5;

import java.util.Scanner;

public class Arrows {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите строку из символов: ‘>’, ‘<’ и ‘-‘");
        System.out.println("Но не больше 106 символов: ");
        String input = scanner.nextLine();

        int count = 0;
        if (input.length() <= 106) {
            count += countOccurrences(input, ">>-->");
            count += countOccurrences(input, "<--<<");
            System.out.println("Количество стрелок: " + count);
        } else {
            System.out.println("Количество символов больше 106");
        }
    }

    private static int countOccurrences(String input, String sub) {
        int count = 0;
        int index = 0;
        while ((index = input.indexOf(sub, index)) != -1) {
            System.out.println("Отслеживаемый кусок: " + input.substring(index, index + 5));
            count++;
            System.out.println("Счет = " + count);
            index += sub.length();
        }
        return count;
    }
}
