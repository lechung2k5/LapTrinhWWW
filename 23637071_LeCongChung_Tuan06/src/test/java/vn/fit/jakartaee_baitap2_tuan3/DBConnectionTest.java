package vn.fit.jakartaee_baitap2_tuan3;

import org.junit.jupiter.api.Test;
import vn.fit.jakartaee_baitap2_tuan3.model.Product;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ProductRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.util.DBConnection;

import java.sql.Connection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DBConnectionTest {

    @Test
    public void testConnection() throws Exception {
        try (Connection conn = DBConnection.getConnection()) {
            assertNotNull(conn);
            System.out.println("Connection successful: " + conn.getCatalog());
        }
    }

    @Test
    public void testFindAll() {
        ProductRepository repo = new ProductRepositoryImpl();
        List<Product> list = repo.findAll();
        System.out.println("Products found: " + list.size());
        for (Product p : list) {
            System.out.println(p.getId() + " - " + p.getName() + " - " + p.getPrice());
        }
        assertFalse(list.isEmpty());
    }
}
