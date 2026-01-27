package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.domain.entity.User;
import sorokin.dev.domain.repository.UserHibernateRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {

    private final UserHibernateRepository userHibernateRepository;
    private final AccountProperties accountProperties;

    public UserService(UserHibernateRepository userHibernateRepository, AccountProperties accountProperties) {
        this.userHibernateRepository = userHibernateRepository;
        this.accountProperties = accountProperties;
    }

    /**
     * Создание пользователя.
     */
    public User create(String login) {
        User user = userHibernateRepository.findByLogin(login);
        if (user != null) {
            throw new IllegalArgumentException("User with login %s already exists".formatted(login));
        }
        BigDecimal firstAmount = accountProperties.getDefaultAmount();
        return userHibernateRepository.save(login, firstAmount);
    }

    /**
     * Поиск пользователя по ID.
     */
    public User findUserById(Long id) {
        return userHibernateRepository.findById(id);
    }

    /**
     * Поиск пользователя по логину.
     */
    public User findUserByLogin(String login) {
        return userHibernateRepository.findByLogin(login);
    }

    /**
     * Получения списка всех пользователей.
     */
    public List<User> getUsers() {
        return userHibernateRepository.findUsersWithActiveAccounts();
    }
}
