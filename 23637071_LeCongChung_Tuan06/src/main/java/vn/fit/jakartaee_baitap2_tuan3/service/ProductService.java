package vn.fit.jakartaee_baitap2_tuan3.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import vn.fit.jakartaee_baitap2_tuan3.dto.RepriceReportDTO;
import vn.fit.jakartaee_baitap2_tuan3.dto.RevenueShareDTO;
import vn.fit.jakartaee_baitap2_tuan3.model.Product;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@ApplicationScoped
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    @Inject
    private ShoppingCartRepository cartRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(int id) {
        return productRepository.findById(id);
    }

    public boolean createProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean updateProduct(Product product) {
        return productRepository.update(product);
    }

    public boolean deleteProduct(int id) {
        return productRepository.delete(id);
    }

    /**
     * Yêu cầu 3: Điều chỉnh giá sản phẩm hàng loạt theo doanh số giỏ hàng
     * - Nếu sản phẩm có tổng lượt đặt >= 5: tăng giá 10% (+10%)
     * - Nếu sản phẩm chưa có lượt đặt nào (quantity = 0): giảm giá 5% (-5%)
     */
    public List<RepriceReportDTO> applyDynamicRepricing() {
        List<Product> products = productRepository.findAll();
        Map<Integer, Integer> orderCounts = cartRepository.getTotalOrderedQuantities();
        List<RepriceReportDTO> report = new ArrayList<>();

        for (Product product : products) {
            int totalQty = orderCounts.getOrDefault(product.getId(), 0);
            double oldPrice = product.getPrice();
            double newPrice = oldPrice;
            String adjustment = "0%";

            if (totalQty >= 5) {
                newPrice = Math.round((oldPrice * 1.10) * 100.0) / 100.0;
                adjustment = "+10%";
            } else if (totalQty == 0) {
                newPrice = Math.round((oldPrice * 0.95) * 100.0) / 100.0;
                adjustment = "-5%";
            }

            if (!adjustment.equals("0%")) {
                product.setPrice(newPrice);
                productRepository.update(product);

                report.add(new RepriceReportDTO(
                        product.getId(),
                        product.getName(),
                        totalQty,
                        oldPrice,
                        newPrice,
                        adjustment
                ));
            }
        }

        return report;
    }

    /**
     * Yêu cầu 5: Thống kê Doanh thu dự kiến & Đóng góp % từng sản phẩm trong giỏ hàng
     */
    public List<RevenueShareDTO> getRevenueShareReport() {
        List<Product> products = productRepository.findAll();
        Map<Integer, Integer> cartQuantities = cartRepository.getTotalOrderedQuantities();

        // Tính doanh thu từng sản phẩm và tổng doanh thu
        double totalRevenue = 0.0;
        List<RevenueShareDTO> list = new ArrayList<>();

        for (Product product : products) {
            int totalUnits = cartQuantities.getOrDefault(product.getId(), 0);
            if (totalUnits > 0) {
                double revenue = Math.round((product.getPrice() * totalUnits) * 100.0) / 100.0;
                totalRevenue += revenue;
                list.add(new RevenueShareDTO(
                        product.getId(),
                        product.getName(),
                        totalUnits,
                        revenue,
                        ""
                ));
            }
        }

        // Tính % đóng góp
        if (totalRevenue > 0) {
            for (RevenueShareDTO dto : list) {
                double percentage = (dto.getRevenueContribution() / totalRevenue) * 100.0;
                dto.setPercentage(String.format(Locale.US, "%.2f%%", percentage));
            }
        }

        // Sắp xếp giảm dần theo doanh thu
        list.sort(Comparator.comparingDouble(RevenueShareDTO::getRevenueContribution).reversed());

        return list;
    }
}
