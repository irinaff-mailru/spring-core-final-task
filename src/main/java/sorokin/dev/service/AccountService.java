package sorokin.dev.service;

import sorokin.dev.dto.Account;
import sorokin.dev.repository.AccountRepository;

import java.math.BigDecimal;

/**
 * Сервис для управления счетами.
 */
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Создание счета.
     */
    public Account create(Long userId) {
        return accountRepository.save(userId);
    }

    /**
     * Пополнение и снятие средств.
     */
    public boolean changeAmount(Long id, BigDecimal summa) {
        Account account = accountRepository.findById(id).orElse(null);
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
