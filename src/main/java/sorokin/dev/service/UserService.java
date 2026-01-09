package sorokin.dev.service;

import org.springframework.stereotype.Service;
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
        if (userRepository.isLoginExist(login)) {
            throw new IllegalArgumentException("User with login %s already exists".formatted(login));
        }
        return userRepository.save(login);
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
        return userRepository.findAll().stream().map(user -> {

            var openAccounts = user.getAccounts().stream()
                    .filter(account -> !account.isClosed())
                    .toList();
            return new User(user.getId(), user.getLogin(), openAccounts);
            }).toList();
    }
}
