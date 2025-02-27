package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    protected Product(String name, BigDecimal price, BigDecimal tax) throws IllegalArgumentException {
        if (name == null || name.equals("")) {
            throw new IllegalArgumentException("Name is invalid");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO)==-1){
            throw new IllegalArgumentException("Price is invalid");
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
    }

    public String getName() {
        return this.name;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public BigDecimal getTaxPercent() {
        return this.taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        BigDecimal newPrice = price.add(price.multiply(taxPercent));
        return newPrice;
    }
}
