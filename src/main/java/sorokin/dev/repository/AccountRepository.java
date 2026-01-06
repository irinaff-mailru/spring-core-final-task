package sorokin.dev.repository;

import org.springframework.stereotype.Repository;
import sorokin.dev.dto.Account;

import java.math.BigDecimal;
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

    public Optional<Account> findById(Long id) {
        return Optional.ofNullable(accounts.get(id));
    }

    public void saveAmount(Long id, BigDecimal amount) {
        var account = findById(id);
        account.ifPresent(value -> value.setMoneyAmount(amount));
    }

    public void closeById(Long id) {
        var account = findById(id);
        account.ifPresent(value -> value.setClosed(true));
    }
}
