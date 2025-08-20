package HW_7;

import java.time.LocalDate;

class DiscountProduct extends Product {
    private double discount;
    private LocalDate expiryDate;

    public DiscountProduct(String name, double price, double discount, LocalDate expiryDate) {
        super(name, price);
        setDiscount(discount);
        this.expiryDate = expiryDate;
    }

    public double getDiscountedPrice() {
        if (LocalDate.now().isAfter(expiryDate)) {
            return getPrice(); 
        }
        return getPrice() - discount;
    }

    public void setDiscount(double discount) {
        if (discount <= 0 || discount >= getPrice()) {
            throw new IllegalArgumentException("Скидка должна быть положительной и меньше стоимости продукта");
        }
        this.discount = discount;
    }

    @Override
    public String toString() {
        return super.toString() + " (Скидка: " + discount + ", Дата окончания: " + expiryDate + ")";
    }
}
