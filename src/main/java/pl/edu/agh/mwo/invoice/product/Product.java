package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public abstract class Product {
    private final String name;

    private final BigDecimal price;

    private final BigDecimal taxPercent;

    private final BigDecimal extraTax;

    protected Product(String name, BigDecimal price, BigDecimal tax, BigDecimal extraTax) {
        if (name == null || name.equals("") || price == null || tax == null
                || tax.compareTo(new BigDecimal(0)) < 0 || price.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException();
        }
        this.name = name;
        this.price = price;
        this.taxPercent = tax;
        this.extraTax = extraTax;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTaxPercent() {
        return taxPercent;
    }

    public BigDecimal getPriceWithTax() {
        return price.multiply(taxPercent).add(price);
    }

    public BigDecimal getExtraTax() {
        return extraTax;
    }
}
