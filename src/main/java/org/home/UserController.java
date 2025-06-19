package org.home;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private List<User> users = new ArrayList<>();

    // Получить всех пользователей
    @GetMapping
    public List<User> getAllUsers() {
        return users;
    }

//    // Получить одного пользователя по ID
//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable int id) {
//        return users.stream()
//                .filter(user -> user.getId() == id)
//                .findFirst()
//                .orElseThrow(() -> new RuntimeException("User not found"));
//    }
//
//    // Создать нового пользователя
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        users.add(user);
//        return user;
//    }

    // Удалить пользователя по ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        users.removeIf(user -> user.getId() == id);
    }
}