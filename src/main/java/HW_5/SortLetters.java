package HW_5;

import java.util.Arrays;
import java.util.Scanner;

public class SortLetters {
    public static void main (String[] args) {
        Scanner scanner = new Scanner(System.in);

        String input = scanner.nextLine();

        String[] words = input.split(" ");

        for (int i = 0; i < words.length; i++) {
            char[] letters = words[i].toLowerCase().toCharArray();
            Arrays.sort(letters);
            words[i] = new String(letters);
        }
        System.out.println(String.join("", words));
        scanner.close();
    }
}
