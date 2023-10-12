import entities.Customer;
import entities.Order;
import entities.Product;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    static List<Product> warehouse = new ArrayList<>();
    static List<Customer> customers = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        initializeWarehouse();
        createCustomers();
        placeOrders();

        printList(orders);

        System.out.println("************* 1 *****************");
        Map<Customer , List<Order> > ordineperCustumer = orders.stream().collect(Collectors.groupingBy(Order::getCustomer));
ordineperCustumer.forEach((customer , ordini) -> System.out.println("Custumer: " + customer + " Ordini: " + ordini));

        System.out.println("************* 2 *****************");
//    Map<Customer, Integer> totSpesaperCliente = orders.stream().collect(Collectors.groupingBy(Order::getCustomer, Collectors.summingInt(Order::getProducts)));

        orders.forEach( order -> {
            List<Product> orderProducts = order.getProducts();
            Customer temp = order.getCustomer();
            double tot = orderProducts.stream().mapToDouble(Product::getPrice).sum();
            System.out.println("Custumer: " + temp.getName() + " Totale: " + tot);
        });


        System.out.println("************* 3 *****************");
     List<Product> prodottiPiuCostosi = warehouse.stream().sorted(Comparator.comparing(Product::getPrice ,Comparator.reverseOrder()) ).limit(3).toList();
     printList(prodottiPiuCostosi);

        System.out.println("************* 4 *****************");
    orders.forEach( order -> {
        List<Product> orderProducts = order.getProducts();
        double media = orderProducts.stream().mapToDouble(Product::getPrice).average().orElse(0.0);
        System.out.println("Custumer: " + order.getCustomer() + " Media: " + media);
    });


        System.out.println("************* 5 *****************");

        Map<String, Double> totPerCategoria = warehouse.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
        totPerCategoria.forEach((category, price ) -> System.out.println("Category: " + category + " Totale: " + price));


    }




    public static void printList(List<?> l) {
        for (Object obj : l) {
            System.out.println(obj);
        }
    }

    // 1
    public static List<Product> getFilteredBooks() {
        return warehouse.stream().filter(product -> product.getCategory().equals("Books") && product.getPrice() > 100).toList();
    }

    // 2
    public static List<Order> getBabyOrders() {
        return orders.stream().filter(order -> order.getProducts().stream().anyMatch(product -> product.getCategory().equals("Baby"))).toList();
    }

    // 3
    public static List<Product> getBoysProductsWithDiscount() {
        return warehouse.stream()
                .filter(p -> p.getCategory().equals("Boys"))
                .map(product -> {
                    Product newProduct = new Product(product.getName(), product.getCategory(), product.getPrice() * 0.90);
                    newProduct.setId(product.getId());
                    return newProduct;
                }).toList();
    }

    // 4
    public static List<Product> getTier2Products() {
        List<Order> filteredByTier = orders.stream()
                .filter(order -> order.getCustomer().getTier() == 2
                        && order.getOrderDate().isBefore(LocalDate.parse("2023-05-09"))
                        && order.getOrderDate().isAfter(LocalDate.parse("2023-05-01")))
                .toList();

        List<Product> products = new ArrayList<>();

        for (Order order : filteredByTier) {
            products.addAll(order.getProducts());
        }
        return products;
    }

    public static void initializeWarehouse() {
        Product iPhone = new Product("IPhone", "Smartphones", 2000.0);
        Product lotrBook = new Product("LOTR", "Books", 101);
        Product itBook = new Product("IT", "Books", 2);
        Product davinciBook = new Product("Da Vinci's Code", "Books", 2);
        Product diapers = new Product("Pampers", "Baby", 3);
        Product toyCar = new Product("Car", "Boys", 15);
        Product toyPlane = new Product("Plane", "Boys", 25);
        Product lego = new Product("Lego Star Wars", "Boys", 500);
        Product[] products = {iPhone, lotrBook, itBook, davinciBook, diapers, toyCar, toyPlane, lego};

        warehouse.addAll(Arrays.asList(products));
    }

    public static void createCustomers() {
        Customer aldo = new Customer("Aldo Baglio", 1);
        Customer giovanni = new Customer("Giovanni Storti", 2);
        Customer giacomo = new Customer("Giacomo Poretti", 3);
        Customer marina = new Customer("Marina Massironi", 2);

        customers.add(aldo);
        customers.add(giovanni);
        customers.add(giacomo);
        customers.add(marina);
    }

    public static void placeOrders() {
        Order aldoOrder = new Order(customers.get(0));
        Order giovanniOrder = new Order(customers.get(1));
        Order giacomoOrder = new Order(customers.get(2));
        Order marinaOrder = new Order(customers.get(3));

        Product iPhone = warehouse.get(0);
        Product lotrBook = warehouse.get(1);
        Product itBook = warehouse.get(2);
        Product davinciBook = warehouse.get(3);
        Product diaper = warehouse.get(4);

        aldoOrder.addProduct(iPhone);
        aldoOrder.addProduct(lotrBook);
        aldoOrder.addProduct(diaper);

        giovanniOrder.addProduct(itBook);
        giovanniOrder.addProduct(davinciBook);
        giovanniOrder.addProduct(iPhone);

        giacomoOrder.addProduct(lotrBook);
        giacomoOrder.addProduct(diaper);

        marinaOrder.addProduct(diaper);

        orders.add(aldoOrder);
        orders.add(giovanniOrder);
        orders.add(giacomoOrder);
        orders.add(marinaOrder);

    }
}