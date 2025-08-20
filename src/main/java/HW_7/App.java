package HW_7;

import java.time.LocalDate;
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
        System.out.println("Пример ввода 'название продукта' = 'стоимость' или 'название продукта' = 'стоимость' скидка 'сумма' 'дата окончания'");
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
            String[] priceAndDiscount = data[1].trim().split(" ");
            double price;
            try {
                price = Double.parseDouble(priceAndDiscount[0]);
                if (priceAndDiscount.length == 4 && priceAndDiscount[1].equals("скидка")) {
                    double discount = Double.parseDouble(priceAndDiscount[2]);
                    LocalDate expiryDate = LocalDate.parse(priceAndDiscount[3]);
                    products.add(new DiscountProduct(productName, price, discount, expiryDate));
                } else {
                    products.add(new Product(productName, price));
                }
            } catch (NumberFormatException e) {
                System.out.println("Стоимость должна быть числом.");
            } catch (Exception e) {
                System.out.println("Ошибка при создании продукта: " + e.getMessage());
            }
        }

        if (buyers.isEmpty()) {
            System.out.println("Список покупателей не может быть пустым. Программа завершена.");
            return;
        }
        if (products.isEmpty()) {
            System.out.println("Список продуктов не может быть пустым. Программа завершена.");
            return;
        }

        System.out.println("Покупатели выбирают продукты ");
        System.out.println("пример ввода 'Имя покупателя' = 'Название продукта' ");
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
