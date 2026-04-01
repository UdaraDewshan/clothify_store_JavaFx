package service;

import model.entity.Product;
import model.entity.Supplier;
import repository.ProductRepository;
import repository.SupplierRepository;

import java.util.List;

public class ProductService {

    private final ProductRepository productRepo = new ProductRepository();
    private final SupplierRepository supplierRepo = new SupplierRepository();

    public boolean addProduct(Product product) {
        return productRepo.save(product);
    }

    public boolean updateProduct(Product product) {
        return productRepo.update(product);
    }

    public boolean deleteProduct(String id) {
        return productRepo.delete(id);
    }

    public List<Product> getAllProducts() {
        return productRepo.getAll();
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepo.getAll();
    }

    public String generateNextProductId() {
        Product lastProduct = productRepo.getLastProduct();
        if (lastProduct != null) {
            String lastId = lastProduct.getProductId();
            int nextIdNum = Integer.parseInt(lastId.replace("P", "")) + 1;
            return String.format("P%03d", nextIdNum);
        }
        return "P001";
    }
}