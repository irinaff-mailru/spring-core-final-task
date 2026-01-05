package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.User;
import sorokin.dev.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создание пользователя.
     */
    public User create(String login) {
        User newUser = userRepository.save(login);
        return newUser;
    }

    /**
     * Поиск пользователя по ID.
     */
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Поиск пользователя по логину.
     */
    public Optional<User> findUserByLogin(String login) {
        return userRepository.findUserByLogin(login);
    }

    /**
     * Получения списка всех пользователей.
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
