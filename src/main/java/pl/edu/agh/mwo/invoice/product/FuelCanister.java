package pl.edu.agh.mwo.invoice.product;

import java.math.BigDecimal;

public class FuelCanister extends TaxFreeProduct {

    public FuelCanister(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public BigDecimal getPriceWithTax() {
        BigDecimal fullPrice = getPrice().add(getExtraTax());
        return fullPrice;
    }
}
