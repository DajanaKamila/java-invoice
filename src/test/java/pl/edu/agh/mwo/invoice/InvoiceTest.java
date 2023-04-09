package pl.edu.agh.mwo.invoice;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.mwo.invoice.Invoice;
import pl.edu.agh.mwo.invoice.product.*;


public class InvoiceTest {
    private Invoice invoice;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void createEmptyInvoiceForTheTest() {
        invoice = new Invoice();
    }

    @Test
    public void testEmptyInvoiceHasEmptySubtotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTaxAmount() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testEmptyInvoiceHasEmptyTotal() {
        Assert.assertThat(BigDecimal.ZERO, Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithTwoDifferentProducts() {
        Product onions = new TaxFreeProduct("Warzywa", new BigDecimal("10"));
        Product apples = new TaxFreeProduct("Owoce", new BigDecimal("10"));
        invoice.addProduct(onions);
        invoice.addProduct(apples);
        Assert.assertThat(new BigDecimal("20"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceSubtotalWithManySameProducts() {
        Product onions = new TaxFreeProduct("Warzywa", BigDecimal.valueOf(10));
        invoice.addProduct(onions, 100);
        Assert.assertThat(new BigDecimal("1000"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasTheSameSubtotalAndTotalIfTaxIsZero() {
        Product taxFreeProduct = new TaxFreeProduct("Warzywa", new BigDecimal("199.99"));
        invoice.addProduct(taxFreeProduct);
        Assert.assertThat(invoice.getNetTotal(), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasProperSubtotalForManyProducts() {
        invoice.addProduct(new TaxFreeProduct("Owoce", new BigDecimal("200")));
        invoice.addProduct(new DairyProduct("Maslanka", new BigDecimal("100")));
        invoice.addProduct(new OtherProduct("Wino", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("310"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasProperTaxValueForManyProduct() {
        // tax: 0
        invoice.addProduct(new TaxFreeProduct("Pampersy", new BigDecimal("200")));
        // tax: 8
        invoice.addProduct(new DairyProduct("Kefir", new BigDecimal("100")));
        // tax: 2.30
        invoice.addProduct(new OtherProduct("Piwko", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("10.30"), Matchers.comparesEqualTo(invoice.getTaxTotal()));
    }

    @Test
    public void testInvoiceHasProperTotalValueForManyProduct() {
        // price with tax: 200
        invoice.addProduct(new TaxFreeProduct("Maskotki", new BigDecimal("200")));
        // price with tax: 108
        invoice.addProduct(new DairyProduct("Maslo", new BigDecimal("100")));
        // price with tax: 12.30
        invoice.addProduct(new OtherProduct("Chipsy", new BigDecimal("10")));
        Assert.assertThat(new BigDecimal("320.30"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test
    public void testInvoiceHasPropoerSubtotalWithQuantityMoreThanOne() {
        // 2x kubek - price: 10
        invoice.addProduct(new TaxFreeProduct("Kubek", new BigDecimal("5")), 2);
        // 3x kozi serek - price: 30
        invoice.addProduct(new DairyProduct("Kozi Serek", new BigDecimal("10")), 3);
        // 1000x pinezka - price: 10
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("50"), Matchers.comparesEqualTo(invoice.getNetTotal()));
    }

    @Test
    public void testInvoiceHasPropoerTotalWithQuantityMoreThanOne() {
        // 2x chleb - price with tax: 10
        invoice.addProduct(new TaxFreeProduct("Chleb", new BigDecimal("5")), 2);
        // 3x chedar - price with tax: 32.40
        invoice.addProduct(new DairyProduct("Chedar", new BigDecimal("10")), 3);
        // 1000x pinezka - price with tax: 12.30
        invoice.addProduct(new OtherProduct("Pinezka", new BigDecimal("0.01")), 1000);
        Assert.assertThat(new BigDecimal("54.70"), Matchers.comparesEqualTo(invoice.getGrossTotal()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithZeroQuantity() {
        invoice.addProduct(new TaxFreeProduct("Tablet", new BigDecimal("1678")), 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvoiceWithNegativeQuantity() {
        invoice.addProduct(new DairyProduct("Zsiadle mleko", new BigDecimal("5.55")), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddingNullProduct() {
        invoice.addProduct(null);
    }

    // Mamy w ogóle numer
    @Test
    public void testInvoiceHasNumber(){
        int number = invoice.getNumber();
        Assert.assertNotNull(number);
    }


    @Test
    public void testInvoiceNumberIsGreaterThan0(){
        int number = invoice.getNumber();
        Assert.assertTrue(number>0);
    }


    @Test
    public void testInvoiceNumberGetsLargerInEveryNewInvoice(){
        Invoice invoice2 = new Invoice();
        int numberInvoice1 = invoice.getNumber();
        int numberInvoice2 = invoice2.getNumber();
        Assert.assertTrue(numberInvoice2 > numberInvoice1);
    }

    @Test
    public void testPrintInvoiceWithEmptyProducts() {
        System.setOut(new PrintStream(outContent));
        String expectedOutput = "";
        invoice.printInvoice();
        String actualOutput = outContent.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testPrintInvoiceWithOneProduct() {
        System.setOut(new PrintStream(outContent));
        DairyProduct product = new DairyProduct("Milk", new BigDecimal(3.5));
        invoice.addProduct(product);
        String expectedOutput = "Invoice number: 10000\n" +
                "Name \tQuantity \tPrice\n" +
                "Milk\t1\t3.5\n" +
                "Number of positions: 1";
        invoice.printInvoice();
        String actualOutput = outContent.toString();
        Assert.assertEquals(expectedOutput, actualOutput);
    }

    @Test
    public void testAddingTwoTheSameProductsMakesProductListLengthOne(){
        DairyProduct product = new DairyProduct("Milk", new BigDecimal(3.5));
        invoice.addProduct(product);
        invoice.addProduct(product);
        Assert.assertEquals(1, invoice.getProducts().size());
    }

    @Test
    public void testAddingTwoTheSameProductsInDifferentQuantityMakesTheListLenghtOne(){
        DairyProduct product = new DairyProduct("Milk", new BigDecimal(3.5));
        invoice.addProduct(product,3);
        invoice.addProduct(product,5);
        Assert.assertEquals(1, invoice.getProducts().size());
    }

    @Test
    public void testAddingFourProducts2DifferntTypesMakesListLengthTwo(){
        DairyProduct product = new DairyProduct("Milk", new BigDecimal(3.5));
        TaxFreeProduct product2 = new TaxFreeProduct("Fuel", new BigDecimal(10));
        invoice.addProduct(product);
        invoice.addProduct(product);
        invoice.addProduct(product2);
        invoice.addProduct(product2);
        Assert.assertEquals(2, invoice.getProducts().keySet().size());
    }

    @Test
    public void testAddingTwoTheSameProductsChangeQuantityOfTwo() {
        DairyProduct product = new DairyProduct("Butter", new BigDecimal("6.00"));
        invoice.addProduct(product);
        invoice.addProduct(product);
        int result = invoice.getProducts().get(product);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testAddingTwoTheSameProductsInQuantityOfThreeAndFiveChangeQuantityOfEight() {
        DairyProduct product = new DairyProduct("Milk", new BigDecimal(3.5));
        TaxFreeProduct product2 = new TaxFreeProduct("Fuel", new BigDecimal(10));
        invoice.addProduct(product, 3);
        invoice.addProduct(product2, 5);
        int result = invoice.getProducts().get(product) + invoice.getProducts().get(product2);
        Assert.assertEquals(8, result);
    }


    @Test
    public void testBottleOfWineCostsHaveAdditionalTaxIncluded() {
        BottleOfWine bottleOfWine = new BottleOfWine("Antares", new BigDecimal(10.00));
        invoice.addProduct(bottleOfWine);
        Assert.assertThat(new BigDecimal("17.86"), Matchers.comparesEqualTo(invoice.getGrossTotal()
                .setScale(2, RoundingMode.HALF_UP)));
    }

    @Test
    public void testFuealCanisterCostsHaveAdditionalTaxIncludedButNoNormalTax() {
        FuelCanister fuelCanister = new FuelCanister("ON 95", new BigDecimal(10.00));
        invoice.addProduct(fuelCanister);
        Assert.assertThat(new BigDecimal("15.56"), Matchers.comparesEqualTo(invoice.getGrossTotal()
                .setScale(2, RoundingMode.HALF_UP)));

    }


}
