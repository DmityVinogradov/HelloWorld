package HW_3;

import java.util.Random;
import java.util.Scanner;

public class APP {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();

        TV[] tv = new TV[3];

        for (int i = 0; i < 3; i++){
            System.out.println("Enter parameters for Television " + (i + 1) + ":");
            String brand = scanner.nextLine();

            System.out.print("Screen Size (Размер от 32 до 112 дюймов): ");
            int screenSize = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Smart TV (yes/no): ");
            String bool = scanner.nextLine();
            boolean smartTV = bool.equals("yes") || bool.equals("no");
            tv[i] = new TV(brand, screenSize, smartTV);
        }
        for (int i = 0; i < tv.length; i++) {
            System.out.println(i + 1);
            tv[i].displayInfo();
        }
        for (int i = 0; i < 3; i++) {
            String[] brands = {"Philips", "TCL", "Xiaomi", "Hisense"};
            String brand = brands[random.nextInt(brands.length)];
            int screenSize = 32 + random.nextInt(81);
            boolean smartTV = random.nextBoolean();
            TV randovTV = new TV(brand, screenSize, smartTV);
                System.out.println();
                randovTV.displayInfo();
        }
        scanner.close();
    }
}
