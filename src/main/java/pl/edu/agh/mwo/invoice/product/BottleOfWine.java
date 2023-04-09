package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class BottleOfWine extends OtherProduct {

    public BottleOfWine(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public BigDecimal getPriceWithTax() {
        BigDecimal fullPrice = getPrice().multiply(getTaxPercent()).add(getPrice()).add(getExtraTax());
        return fullPrice;
    }
}
