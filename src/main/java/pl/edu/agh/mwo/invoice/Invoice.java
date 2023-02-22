package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    //private Collection<Product> products ;
    private Map<Product, Integer> products = new HashMap<>();
    private Set<Product> listOfProducts = products.keySet();

    public void addProduct(Product product) {
        this.products.put(product, 1);
    }

    public void addProduct(Product product, Integer quantity) {
        this.products.put(product, quantity);
    }

    public BigDecimal getNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;

        if (!products.isEmpty()){
            for (Product p : listOfProducts){
               netPrice = netPrice.add(p.getPrice().multiply((BigDecimal.valueOf(products.get(p)))));
            }
        }
        return netPrice;
    }

    public BigDecimal getTax() {
        BigDecimal tax = BigDecimal.ZERO;
        if (!products.isEmpty()) {
            for (Product p : listOfProducts){
                BigDecimal taxToAdd = p.getPriceWithTax().subtract(p.getPrice()).multiply(BigDecimal.valueOf(products.get(p)));
                tax = tax.add(taxToAdd);
            }
        }
        return tax;
    }

    public BigDecimal getTotal() {
        BigDecimal totalPrice = BigDecimal.ZERO;
        if (!products.isEmpty()) {
            totalPrice = getNetPrice().add(getTax());
        }
        return totalPrice;
    }
}
