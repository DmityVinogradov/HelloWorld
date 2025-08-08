package attestation.attestation01;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Person> buyers = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        System.out.println("Введите покупателей (Имя и сумма денег)");
        System.out.println("пример ввода: 'Имя' = 'сумма денег' ");
        System.out.println("для завершения введите 'END':");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("END")) {
                break;
            }
            String[] data = input.split("=");
            if (data.length != 2) {
                System.out.println("Неверный ввод. Попробуйте еще раз.");
                continue;
            }
            String name = data[0].trim();
            double money;
            try {
                money = Double.parseDouble(data[1].trim());
                buyers.add(new Person(name, money));
            } catch (NumberFormatException e) {
                System.out.println("Деньги должны быть числом.");
            }
        }

        System.out.println("Введите продукты:");
        System.out.println("Пример ввода 'название продуктв' = 'стоимость' ");
        System.out.println("для завершения введите 'END':");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("END")) {
                break;
            }
            String[] data = input.split("=");
            if (data.length != 2) {
                System.out.println("Неверный ввод. Попробуйте еще раз.");
                continue;
            }
            String productName = data[0].trim();
            double price;
            try {
                price = Double.parseDouble(data[1].trim());
                products.add(new Product(productName, price));
            } catch (NumberFormatException e) {
                System.out.println("Стоимость должна быть числом.");
            }

        }
        if (buyers.isEmpty()) {
            System.out.println("Список покупателей не может быть пустыми. Программа завершена.");
            return;
        }
        if (products.isEmpty()) {
            System.out.println("Список продуктов не может быть пустыми. Программа завершена.");
            return;
        }

        System.out.println("Покупатели выбирают продукты ");
        System.out.println("пример ввода 'Имя покупателя' = 'Название продуктв' ");
        System.out.println("для завершения введите 'END':");
        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("END")) {
                break;
            }
            String[] data = input.split(" = ");
            if (data.length != 2) {
                System.out.println("Неверный ввод. Попробуйте еще раз.");
                continue;
            }
            String buyerName = data[0].trim();
            String productName = data[1].trim();

            Person buyer = buyers.stream()
                    .filter(b -> b.getName().equals(buyerName))
                    .findFirst()
                    .orElse(null);
            Product product = products.stream()
                    .filter(p -> p.getName().equals(productName))
                    .findFirst()
                    .orElse(null);

            if (buyer != null && product != null) {
                buyer.addProduct(product);
            } else {
                System.out.println("Покупатель или продукт не найден.");
            }
        }

        System.out.println("\nРезультаты покупок:");
        for (Person buyer : buyers) {
            buyer.printCart();
        }

        scanner.close();
    }
}
