package sorokin.dev.repository;

import org.springframework.stereotype.Repository;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class AccountRepository {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public Account save(Long userId, BigDecimal amount) {
        Long id = idGenerator.getAndIncrement();
        Account newAccount = new Account(id, userId, amount);
        accounts.put(id, newAccount);
        return newAccount;
    }

    public List<Account> findAllByUserId(Long userId) {
        return accounts.values().stream()
                .filter(a -> userId.equals(a.getUserId())).toList();
    }

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public boolean closeById(Long id) {
        return accounts.remove(id) != null;
    }
}
