package com.empresa.demo.service;

import com.empresa.demo.model.User;

public class UserService {
    public User findById(Long id) {
        // TODO: simular recuperación (p.ej. base de datos)
        return new User(id, "NombreEjemplo", "ejemplo@empresa.com");
    }
}
