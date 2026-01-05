package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.dto.Account;
import sorokin.dev.repository.AccountRepository;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * Сервис для управления счетами.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Создание счета.
     */
    public Account create(Long userId, BigDecimal amount) {
        return accountRepository.save(userId, amount);
    }

    /**
     * Поиск счета по ID.
     */
    public Optional<Account> findAccountById(Long id) {
        return accountRepository.findById(id);
    }

    /**
     * Пополнение средств.
     */
    public boolean addAmount(Long id, BigDecimal amount) {
        var account = accountRepository.findById(id);
        if (account.isEmpty()) {
            return false;
        }
        accountRepository.putDeposit(id, amount);
        return true;
    }

    /**
     * Cнятие средств.
     */
    public boolean withdraw(Long id, BigDecimal amount) {
        var account = accountRepository.findById(id);
        if (account.isEmpty()) {
            return false;
        }
        accountRepository.putDeposit(id, amount);
        return true;
    }

    /**
     * Перевод средств между счетами.
     */
    public boolean transferAmount() {
        return true;
    }

    /**
     * Закрытие счета.
     */
    public boolean close(Long id) {
        Account account = accountRepository.findById(id).orElse(null);
        accountRepository.closeById(id);
        return true;
    }
}
