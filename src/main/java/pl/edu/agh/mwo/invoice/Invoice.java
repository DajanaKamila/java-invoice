package pl.edu.agh.mwo.invoice;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.edu.agh.mwo.invoice.product.Product;

public class Invoice {
    //private Collection<Product> products ;
    private Map<Product, Integer> products = new HashMap<>();
    public void addProduct(Product product) {
        this.products.put(product, null);
    }

    public void addProduct(Product product, Integer quantity) {
        this.products.put(product, quantity);
    }

    public BigDecimal getNetPrice() {
        BigDecimal netPrice = BigDecimal.ZERO;
        Set<Product> listOfProducts = products.keySet();
        if (!products.isEmpty()){
            for (Product p : listOfProducts){
               netPrice = netPrice.add(p.getPrice().multiply((BigDecimal.valueOf(products.get(p)))));
            }
        }
        return netPrice;
    }

    public BigDecimal getTax() {
        return BigDecimal.ZERO;
    }

    public BigDecimal getTotal() {
        return BigDecimal.ZERO;
    }
}
