package service;

import model.entity.User;
import repository.UserRepository;

import java.util.List;

public class UserService {

    private final UserRepository userRepo = new UserRepository();

    public boolean addEmployee(User user) {
        return userRepo.save(user);
    }

    public boolean updateEmployee(User user) {
        return userRepo.update(user);
    }

    public boolean deleteEmployee(Long id) {
        return userRepo.delete(id);
    }

    public List<User> getAllEmployees() {
        return userRepo.getAll();
    }

    public User getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
}