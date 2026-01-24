package sorokin.dev.repository;

import org.springframework.stereotype.Repository;
import sorokin.dev.dto.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class UserRepository {

    private final Map<Long, User> users = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public User save(String login) {
        Long id = idGenerator.getAndIncrement();
        User newUser = new User(id, login, new ArrayList<>());
        users.put(id, newUser);
        return newUser;
    }

    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    public Optional<User> findUserByLogin(String login) {
        return users.values().stream()
                .filter(u -> login.equals(u.getLogin()))
                .findFirst();
    }

    public boolean isLoginExist(String login) {
        return users.values().stream()
                .map(User::getLogin)
                .anyMatch(l -> l.equals(login));
    }
}
