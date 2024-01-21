package com.example.repository;

import com.example.pojo.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserRepository{
    private final List<User> userList = new ArrayList<>();

    public User findById(Long id) {
        return userList.stream()
                .filter(user -> user.getId().compareTo(Math.toIntExact(id)) == 0)
                .findFirst()
                .orElse(null);
    }


    @Transactional
    public void persist(User user) {
        userList.add(user);
    }

    @Transactional
    public void delete(User user) {
        userList.remove(user);
    }

}
