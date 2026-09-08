package vn.edu.iuh.cartapi.session;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.SessionScoped;
import vn.edu.iuh.cartapi.service.ShoppingCart;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@SessionScoped
public class UserSession implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final String instanceId = UUID.randomUUID().toString();
    private final ShoppingCart shoppingCart = new ShoppingCart();
    private String username;

    @PostConstruct
    public void created() {
        System.out.println("CREATED UserSession: " + instanceId);
    }

    @PreDestroy
    public void destroyed() {
        System.out.println("DESTROYED UserSession: " + instanceId);
    }

    public void login(String username) {
        this.username = username;
    }

    public boolean isLoggedIn() {
        return username != null;
    }

    public String getUsername() {
        return username;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public ShoppingCart getShoppingCart() {
        return shoppingCart;
    }
}
