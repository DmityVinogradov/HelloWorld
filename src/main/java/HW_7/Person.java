package HW_7;
import java.util.ArrayList;
import java.util.List;

class Person {
    private String name;
    private double money;
    private List<Product> cart;

    public Person(String name, double money) {
        setName(name);
        setMoney(money);
        this.cart = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Имя не может быть короче 3 символов");
        }
        this.name = name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        if (money < 0) {
            throw new IllegalArgumentException("Деньги не могут быть отрицательными");
        }
        this.money = money;
    }

    public void addProduct(Product product) {
        double priceToCheck = product instanceof DiscountProduct ? ((DiscountProduct) product).getDiscountedPrice() : product.getPrice();

        if (priceToCheck > money) {
            System.out.println(name + " не может позволить себе " + product.getName());
        } else {
            cart.add(product);
            money -= priceToCheck;
            System.out.println(name + " купил " + product.getName());
        }
    }


    public void printCart() {
        if (cart.isEmpty()) {
            System.out.println(name + " - Ничего не куплено");
        } else {
            System.out.print(name + " - ");
            for (int i = 0; i < cart.size(); i++) {
                System.out.print(cart.get(i));
                if (i < cart.size() - 1) {
                    System.out.print(", ");
                }
                System.out.println();
            }
        }
        System.out.println("Остаток средств: " + getMoney());
        System.out.println();
    }
}

//Тестовые данные:
/*
Дмитрий = 10000
Игорь = 5000
Поля = 150
Александр = 200
end
Хлеб = 100
Молоко = 67
Яблоко = 120
Сыр = 200 скидка 50 2025-12-31
Шоколад = 150 скидка 30 2025-08-31
Кофе = 300 скидка 100 2025-09-15

end
Поля = Хлеб
Дмитрий = Хлеб
Игорь = Шоколад
Поля = Яблоки
Александр = Сыр
Дмитрий = Кофе
end
*/