package sorokin.dev.service;

import sorokin.dev.dto.User;
import sorokin.dev.repository.UserRepository;

import java.util.List;

/**
 * Сервис для управления пользователями.
 */
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Создание пользователя.
     */
    public User create(String login) {
        return userRepository.save(login);
    }

    /**
     * Поиск пользователя по ID.
     */
    public User findUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Получения списка всех пользователей.
     */
    public List<User> getUsers() {
        return userRepository.findAll();
    }
}
