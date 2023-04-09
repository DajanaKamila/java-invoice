package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends Product {
    public FuelCanister(String name, BigDecimal price, BigDecimal extraTax) {
        super(name, price, BigDecimal.ZERO, extraTax);
    }
}
