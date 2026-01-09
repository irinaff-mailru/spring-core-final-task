package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.dto.Account;
import sorokin.dev.repository.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

/**
 * Сервис для управления счетами.
 */
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountProperties accountProperties;

    public AccountService(AccountRepository accountRepository, AccountProperties accountProperties) {
        this.accountRepository = accountRepository;
        this.accountProperties = accountProperties;
    }

    /**
     * Создание счета.
     */
    public Account create(Long userId, boolean isFirstAccount) {
        BigDecimal amount = isFirstAccount ? accountProperties.getDefaultAmount() : BigDecimal.ZERO;
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

    public List<Account> getAllUserAccounts(Long userId) {
        return accountRepository.findAllByUserId(userId);
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
        Account account = getActiveAccount(id);
        synchronized (account) {
            return updateBalance(id, amount.negate(), true);
        }
    }

    /**
     * Перевод средств между счетами.
     */
    public boolean transferAmount(Long sourceId, Long targetId, BigDecimal amount) {
        Account source = getActiveAccount(sourceId);
        Account target = getActiveAccount(targetId);
        if (source == null || target == null) {
            System.out.println("operation not avalable.");
            return false;
        }

        Account first = (source.getId() < target.getId()) ? source : target;
        Account second = (source.getId() < target.getId()) ? target : source;
        synchronized (first) {
            synchronized (second) {
                if (source.getMoneyAmount().compareTo(amount) < 0) {
                    System.out.println("operation not avalable.");
                    return false;
                }
                BigDecimal amountToDeposit = source.getUserId() == target.getUserId() ? amount :
                        amount.subtract(amount.multiply(accountProperties.getTransferCommission()).setScale(2, RoundingMode.HALF_UP));

                withdrawWithoutBloking(sourceId, amount);
                depositAmount(targetId, amountToDeposit);
            }
        }
        return true;
    }

    /**
     * Закрытие счета.
     */
    public boolean close(Long id) {
        Account account = getActiveAccount(id);
        if (account == null) {
            System.out.println("No such account.");
            return false;
        }
        List<Account> userAccounts = getAllUserAccounts(account.getUserId());
        if (userAccounts.size() < 2) {
            System.out.println("Cannot close the only one account.");
            return false;
        }
        synchronized (account) {
            accountRepository.closeById(id);
            return true;
        }
    }

    private boolean withdrawWithoutBloking(Long id, BigDecimal amount) {
        return updateBalance(id, amount.negate(), true);
    }

    private boolean updateBalance(Long id, BigDecimal delta, boolean isWithdrawal) {
        Account account = getActiveAccount(id);

        if (account == null || (isWithdrawal && account.getMoneyAmount().compareTo(delta.abs()) < 0)) {
            System.out.println("operation not avalable.");
            return false;
        }

        BigDecimal newAmount = account.getMoneyAmount().add(delta).setScale(2, RoundingMode.HALF_UP);
        accountRepository.saveAmount(id, newAmount);
        return true;
    }
}
