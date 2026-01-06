package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.dto.Account;
import sorokin.dev.repository.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
     * Поиск активного счета по ID.
     */
    public Account getActiveAccount(Long id) {
        return accountRepository.findById(id)
                .filter(a -> !a.isClosed())
                .orElse(null);
    }

    /**
     * Пополнение средств.
     */
    public boolean depositAmount(Long id, BigDecimal amount) {
        return updateBalance(id, amount, false);
    }

    /**
     * Cнятие средств.
     */
    public boolean withdraw(Long id, BigDecimal amount) {
        return updateBalance(id, amount.negate(), true);
    }

    /**
     * Перевод средств между счетами.
     */
    public boolean transferAmount(Long sourceId, Long targetId, BigDecimal amount) {
        Account source = getActiveAccount(sourceId);
        Account target = getActiveAccount(targetId);
        if (source == null || target == null || source.getMoneyAmount().compareTo(amount) < 0) {
            System.out.println("operation not avalable.");
            return false;
        }

        Account first = (source.getId() < target.getId()) ? source : target;
        Account second = (source.getId() < target.getId()) ? target : source;

        synchronized (first) {
            synchronized (second) {
                withdraw(sourceId, amount);
                depositAmount(targetId, amount);
            }
        }
        return true;
    }

    /**
     * Закрытие счета.
     */
    public boolean close(Long id) {
        if (getActiveAccount(id) == null) {
            System.out.println("operation not avalable.");
            return false;
        }
        accountRepository.closeById(id);
        return true;
    }

    private boolean updateBalance(Long id, BigDecimal delta, boolean isWithdrawal) {
        Account account = getActiveAccount(id);

        if (account == null ||(isWithdrawal && account.getMoneyAmount().compareTo(delta.abs()) < 0)) {
            System.out.println("operation not avalable.");
            return false;
        }

        BigDecimal newAmount = account.getMoneyAmount().add(delta).setScale(2, RoundingMode.HALF_UP);
        accountRepository.saveAmount(id, newAmount);
        return true;
    }
}
