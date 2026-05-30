package ru.kpfu.itis.subscribio.service;

import ru.kpfu.itis.subscribio.form.RegisterForm;
import ru.kpfu.itis.subscribio.model.User;

public interface UserService {
    User register(RegisterForm form);
    User findByEmail(String email);
}