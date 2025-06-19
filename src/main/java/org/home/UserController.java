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
}