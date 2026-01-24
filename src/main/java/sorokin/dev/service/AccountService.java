package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.dto.Account;
import sorokin.dev.repository.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
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
     * Поиск активного счета по ID.
     */
    public Optional<Account> getActiveAccount(Long id) {
        return accountRepository.findById(id)
                .filter(a -> !a.isClosed());
    }

    public List<Account> getAllActiveUserAccounts(Long userId) {
        return accountRepository.findAllActiveByUserId(userId);
    }

    /**
     * Пополнение средств.
     */
    public void depositAmount(Long id, BigDecimal amount) {
        updateBalance(id, amount, false);
    }

    /**
     * Cнятие средств.
     */
    public void withdraw(Long id, BigDecimal amount) {
        Account account = getActiveAccount(id)
                .orElseThrow(() -> new IllegalArgumentException("No such account ID %s".formatted(id)));
        synchronized (account) {
            updateBalance(id, amount.negate(), true);
        }
    }

    /**
     * Перевод средств между счетами.
     */
    public void transferAmount(Long sourceId, Long targetId, BigDecimal amount) {
        Account source = getActiveAccount(sourceId)
                .orElseThrow(() -> new IllegalArgumentException("No such account ID %s".formatted(sourceId)));
        ;
        Account target = getActiveAccount(targetId)
                .orElseThrow(() -> new IllegalArgumentException("No such account ID %s".formatted(targetId)));
        ;

        Account first = (source.getId() < target.getId()) ? source : target;
        Account second = (source.getId() < target.getId()) ? target : source;
        synchronized (first) {
            synchronized (second) {
                if (source.getMoneyAmount().compareTo(amount) < 0) {
                    throw new IllegalArgumentException(
                            "Cannot withdraw from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                                    .formatted(sourceId, source.getMoneyAmount(), amount)
                    );
                }
                BigDecimal amountToDeposit = source.getUserId() == target.getUserId() ? amount :
                        amount.subtract(amount.multiply(accountProperties.getTransferCommission()).setScale(2, RoundingMode.HALF_UP));

                withdrawWithoutBloking(sourceId, amount);
                depositAmount(targetId, amountToDeposit);
            }
        }
    }

    /**
     * Закрытие счета.
     */
    public Account close(Long id) {
        Account accountToClose = getActiveAccount(id)
                .orElseThrow(() -> new IllegalArgumentException("No such account ID %s".formatted(id)));

        List<Account> userAccounts = getAllActiveUserAccounts(accountToClose.getUserId());
        if (userAccounts.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one account ID %s"
                    .formatted(id));
        }
        Account accountToDeposit = userAccounts.stream()
                .filter(a -> !Objects.equals(a.getId(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found account for deposit amount"));

        Account first = (accountToClose.getId() < accountToDeposit.getId()) ? accountToClose : accountToDeposit;
        Account second = (accountToClose.getId() < accountToDeposit.getId()) ? accountToDeposit : accountToClose;

        synchronized (first) {
            synchronized (second) {
                accountRepository.closeById(id);
                depositAmount(accountToDeposit.getId(), accountToClose.getMoneyAmount());
                return accountToClose;
            }
        }
    }

    private void withdrawWithoutBloking(Long id, BigDecimal amount) {
        updateBalance(id, amount.negate(), true);
    }

    private void updateBalance(Long id, BigDecimal delta, boolean isWithdrawal) {
        Optional<Account> account = getActiveAccount(id);

        if (account.isEmpty() || (isWithdrawal && account.get().getMoneyAmount().compareTo(delta.abs()) < 0)) {
            throw new IllegalArgumentException("Cannot update balance account ID %s"
                    .formatted(id));
        }

        BigDecimal newAmount = account.get().getMoneyAmount().add(delta).setScale(2, RoundingMode.HALF_UP);
        accountRepository.saveAmount(id, newAmount);
    }
}
