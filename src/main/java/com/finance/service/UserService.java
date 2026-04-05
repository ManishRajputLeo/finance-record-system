package com.finance.service;


import com.finance.entity.Role;
import com.finance.entity.Status;
import com.finance.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user);
    List<User> getAllUsers();
    User updateRole(Long id, Role role);
    User updateStatus(Long id, Status status);
}
