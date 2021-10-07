package db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class ProductDatabaseTest {
    private ProductDatabase db;
    private static final List<Product> PRODUCTS = List.of(
            new Product("phone", 777),
            new Product("computer", 10000),
            new Product("apple", 30),
            new Product("Dmozze", 1),
            new Product("oil", -100)
    );

    @BeforeEach
    public void beforeEach() throws SQLException, IOException {
        // In-memory sqlite can take only one connection,
        // but I have a new connection for every query. So I can't use it
        File file = File.createTempFile("database", "testing", new File("."));
        file.deleteOnExit();
        db = new ProductDatabase(file.getAbsolutePath());
        db.create();
    }

    private void saveAllProducts() throws SQLException {
        for (Product p : PRODUCTS) {
            db.save(p);
        }
    }

    @Test
    public void emptyDatabaseTest() throws SQLException {
        List<Product> products = db.findAll();
        assertEquals(Collections.emptyList(), products);
        assertEquals(0, db.getCount());
        assertEquals(0, db.getPricesSum());
        assertNull(db.getMaxByPrice());
        assertNull(db.getMinByPrice());
    }

    @Test
    public void nonEmptyDatabaseTest() throws SQLException {
        saveAllProducts();
        List<Product> products = db.findAll();
        assertEquals(PRODUCTS, products);

        assertEquals(PRODUCTS.size(), db.getCount());

        int sum = PRODUCTS.stream().map(Product::getPrice).reduce(0, Integer::sum);
        assertEquals(sum, db.getPricesSum());

        int min = PRODUCTS.stream().map(Product::getPrice).reduce(0, Integer::min);
        Product minPriceProduct = PRODUCTS.stream().filter(p -> p.getPrice().equals(min)).collect(Collectors.toList()).get(0);
        assertEquals(minPriceProduct, db.getMinByPrice());

        int max = PRODUCTS.stream().map(Product::getPrice).reduce(0, Integer::max);
        Product maxPriceProduct = PRODUCTS.stream().filter(p -> p.getPrice().equals(max)).collect(Collectors.toList()).get(0);
        assertEquals(maxPriceProduct, db.getMaxByPrice());
    }

    @Test
    public void twoMinsTest() throws SQLException {
        Product p1 = new Product("phone", 1);
        Product p2 = new Product("apple", 1);
        Product p3 = new Product("TV", 33);

        db.save(p1);
        db.save(p2);
        db.save(p3);

        Product minPriceProduct = db.getMinByPrice();
        assertTrue(Set.of("apple", "phone").contains(minPriceProduct.getName()));
    }

    @Test
    public void twoMaxsTest() throws SQLException {
        Product p1 = new Product("phone", 1);
        Product p2 = new Product("apple", 33);
        Product p3 = new Product("TV", 33);

        db.save(p1);
        db.save(p3);
        db.save(p2);

        Product maxPriceProduct = db.getMaxByPrice();
        assertTrue(Set.of("apple", "TV").contains(maxPriceProduct.getName()));
    }

    @Test
    public void dropDataTest() throws SQLException {
        saveAllProducts();
        db.clearData();
        List<Product> products = db.findAll();
        assertEquals(Collections.emptyList(), products);
        assertEquals(0, db.getCount());
        assertEquals(0, db.getPricesSum());
        assertNull(db.getMaxByPrice());
        assertNull(db.getMinByPrice());
    }
}
