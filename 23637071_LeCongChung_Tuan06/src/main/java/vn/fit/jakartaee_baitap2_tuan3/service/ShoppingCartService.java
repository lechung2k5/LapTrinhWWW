package vn.fit.jakartaee_baitap2_tuan3.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.fit.jakartaee_baitap2_tuan3.dto.AddCartResponseDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CartBillItemDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CheckoutSummaryDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.CustomerBillDTO;
import vn.fit.jakartaee_baitap2_tuan3.model.Product;
import vn.fit.jakartaee_baitap2_tuan3.model.ShoppingCart;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ShoppingCartService {

    @Inject
    private ShoppingCartRepository cartRepository;

    @Inject
    private ProductRepository productRepository;

    public List<ShoppingCart> getAllCarts() {
        return cartRepository.findAll();
    }

    public ShoppingCart getCartById(int id) {
        return cartRepository.findById(id);
    }

    public boolean createCart(ShoppingCart cart) {
        return cartRepository.save(cart);
    }

    public boolean updateCart(ShoppingCart cart) {
        return cartRepository.update(cart);
    }

    public boolean deleteCart(int id) {
        return cartRepository.delete(id);
    }

    /**
     * Yêu cầu 1: Tính tổng hóa đơn giỏ hàng của một khách hàng (Customer Cart Bill Breakdown)
     * - Giảm 10% nếu subTotal > $2,000
     */
    public CustomerBillDTO calculateCustomerBill(String customerName) {
        List<ShoppingCart> items = cartRepository.findByCustomerName(customerName);
        List<CartBillItemDTO> billItems = new ArrayList<>();
        double subTotal = 0.0;

        for (ShoppingCart cart : items) {
            Product product = productRepository.findById(cart.getProductId());
            if (product != null) {
                double itemPrice = product.getPrice();
                double itemTotal = itemPrice * cart.getQuantity();
                subTotal += itemTotal;

                billItems.add(new CartBillItemDTO(
                        cart.getId(),
                        product.getName(),
                        itemPrice,
                        cart.getQuantity(),
                        Math.round(itemTotal * 100.0) / 100.0
                ));
            }
        }

        subTotal = Math.round(subTotal * 100.0) / 100.0;
        double discount = 0.0;
        if (subTotal > 2000.0) {
            discount = Math.round((subTotal * 0.10) * 100.0) / 100.0;
        }
        double finalTotal = Math.round((subTotal - discount) * 100.0) / 100.0;

        return new CustomerBillDTO(customerName, billItems, subTotal, discount, finalTotal);
    }

    /**
     * Yêu cầu 2: Thêm sản phẩm vào giỏ hàng có kiểm tra ràng buộc giá trị tối đa (Add Item with Value Cap)
     * - Chặn nếu giá trị lô hàng > $5,000
     */
    public AddCartResponseDTO addItemToCart(ShoppingCart cart) {
        Product product = productRepository.findById(cart.getProductId());
        if (product == null) {
            throw new IllegalArgumentException("Sản phẩm không tồn tại!");
        }

        double estimatedTotal = product.getPrice() * cart.getQuantity();
        if (estimatedTotal > 5000.0) {
            throw new IllegalStateException("Giá trị đơn hàng vượt quá hạn mức cho phép ($5,000)!");
        }

        cartRepository.save(cart);
        return new AddCartResponseDTO(
                cart.getId(),
                product.getName(),
                cart.getQuantity(),
                Math.round(estimatedTotal * 100.0) / 100.0
        );
    }

    /**
     * Yêu cầu 4: Chuyển đổi toàn bộ giỏ hàng của khách thành Đơn đặt mua và Xóa giỏ (Checkout & Clear Cart)
     */
    public CheckoutSummaryDTO checkout(String customerName) {
        List<ShoppingCart> items = cartRepository.findByCustomerName(customerName);
        int totalItems = items.size();
        int totalQuantity = 0;
        double totalPaid = 0.0;

        for (ShoppingCart cart : items) {
            totalQuantity += cart.getQuantity();
            Product product = productRepository.findById(cart.getProductId());
            if (product != null) {
                totalPaid += product.getPrice() * cart.getQuantity();
            }
        }

        totalPaid = Math.round(totalPaid * 100.0) / 100.0;

        // Xóa giỏ hàng sau khi checkout
        cartRepository.deleteByCustomerName(customerName);

        return new CheckoutSummaryDTO(
                customerName,
                totalItems,
                totalQuantity,
                totalPaid,
                "CHECKOUT_COMPLETED_AND_CART_CLEARED"
        );
    }
}
