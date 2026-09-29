package vn.fit.jakartaee_baitap2_tuan3.config;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import vn.fit.jakartaee_baitap2_tuan3.repository.ProductRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.ShoppingCartRepository;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ProductRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.repository.impl.ShoppingCartRepositoryImpl;
import vn.fit.jakartaee_baitap2_tuan3.service.ProductService;
import vn.fit.jakartaee_baitap2_tuan3.service.ShoppingCartService;

public class AppConfig extends ResourceConfig {
    public AppConfig() {
        packages("vn.fit.jakartaee_baitap2_tuan3.controller");
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bindAsContract(ProductRepositoryImpl.class).to(ProductRepository.class).in(jakarta.inject.Singleton.class);
                bindAsContract(ShoppingCartRepositoryImpl.class).to(ShoppingCartRepository.class).in(jakarta.inject.Singleton.class);
                bindAsContract(ProductService.class).in(jakarta.inject.Singleton.class);
                bindAsContract(ShoppingCartService.class).in(jakarta.inject.Singleton.class);
            }
        });
    }
}
