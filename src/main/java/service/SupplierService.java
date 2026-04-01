package service;

import model.entity.Supplier;
import repository.SupplierRepository;

import java.util.List;

public class SupplierService {

    private final SupplierRepository supplierRepo = new SupplierRepository();

    public boolean addSupplier(Supplier supplier) {
        return supplierRepo.save(supplier);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepo.getAll();
    }

    public String generateNextSupplierId() {
        Supplier lastSupplier = supplierRepo.getLastSupplier();
        if (lastSupplier != null) {
            String lastId = lastSupplier.getSupplierId();
            int nextIdNum = Integer.parseInt(lastId.replace("S", "")) + 1;
            return String.format("S%03d", nextIdNum);
        }
        return "S001";
    }
}