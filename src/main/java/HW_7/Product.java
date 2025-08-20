package HW_7;

class Product {
    private String name;
    private double price;

    public Product(String name, double price) {
        setName(name);
        setPrice(price);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isEmpty() || name.matches("\\d+")) {
            throw new IllegalArgumentException("Название продукта не может быть пустым или содержать только цифры");
        }
        if (name.length() < 3) {
            throw new IllegalArgumentException("Название продукта не может быть короче 3 символов");
        }
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Стоимость продукта не может быть нулевой или отрицательной");
        }
        this.price = price;
    }

    @Override
    public String toString() {
        return name + " (Цена: " + price + ")";
    }
}
