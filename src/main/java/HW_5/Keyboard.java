package HW_5;

import java.util.Scanner;

public class Keyboard {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите букву");
        char input = scanner.nextLine().charAt(0);

        String keyboard = "qwertyuiopasdfghjklzxcvbnm";
        int index = keyboard.indexOf(input);
        if (index!= -1) {
            int leftIndex = (index - 1 + keyboard.length()) % keyboard.length();
            System.out.println(keyboard.charAt(leftIndex));
        } else {
            System.out.println("Неверный ввод!");
        }

        scanner.close();
    }
}
