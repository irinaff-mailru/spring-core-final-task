package sorokin.dev.service;

import org.springframework.stereotype.Service;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.domain.entity.Account;
import sorokin.dev.domain.entity.User;
import sorokin.dev.domain.repository.AccountHibernateRepository;
import sorokin.dev.domain.repository.UserHibernateRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

/**
 * Сервис для управления счетами.
 */
@Service
public class AccountService {

    private final AccountHibernateRepository accountHibernateRepository;
    private final AccountProperties accountProperties;
    private final UserHibernateRepository userHibernateRepository;

    public AccountService(AccountHibernateRepository accountHibernateRepository, AccountProperties accountProperties, UserHibernateRepository userHibernateRepository) {
        this.accountHibernateRepository = accountHibernateRepository;
        this.accountProperties = accountProperties;
        this.userHibernateRepository = userHibernateRepository;
    }

    /**
     * Создание счета.
     */
    public Account create(Long userId) {
        User user = userHibernateRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Not found user with userId %s".formatted(userId));
        }
        BigDecimal amount = BigDecimal.ZERO;
        return accountHibernateRepository.save(user, amount);
    }

    /**
     * Поиск активного счета по ID.
     */
    public Account getActiveAccount(Long id) {
        Account account = accountHibernateRepository.findById(id);
        if (account == null) {
            throw new IllegalArgumentException("No such account ID %s".formatted(id));
        }

        if (account.isClosed()) {
            throw new IllegalArgumentException("Account with ID %s already closed".formatted(id));
        }
        return account;
    }

    public List<Account> getAllActiveUserAccounts(Long userId) {
        return accountHibernateRepository.findAllActiveByUserId(userId);
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
        updateBalance(id, amount.negate(), true);
    }

    /**
     * Перевод средств между счетами.
     */
    public void transferAmount(Long sourceId, Long targetId, BigDecimal amount) {
        Account source = getActiveAccount(sourceId);
        if (source == null) {
            throw new IllegalArgumentException("No such source account ID %s".formatted(sourceId));
        }
        Account target = getActiveAccount(targetId);
        if (target == null) {
            throw new IllegalArgumentException("No such target account ID %s".formatted(targetId));
        }
        if (source.getMoneyAmount().compareTo(amount) < 0) {
            throw new IllegalArgumentException(
                    "Cannot withdraw from account: id=%s, moneyAmount=%s, attemptedWithdraw=%s"
                            .formatted(sourceId, source.getMoneyAmount(), amount)
            );
        }
        BigDecimal amountToDeposit = source.getUser().getId().equals(target.getUser().getId()) ? amount :
                amount.subtract(amount.multiply(accountProperties.getTransferCommission()).setScale(2, RoundingMode.HALF_UP));

        accountHibernateRepository.transferAmount(source, target, amount, amountToDeposit);

    }

    /**
     * Закрытие счета.
     */
    public void close(Long id) {
        Account accountToClose = getActiveAccount(id);

        List<Account> userAccounts = getAllActiveUserAccounts(accountToClose.getUser().getId());
        if (userAccounts.size() == 1) {
            throw new IllegalArgumentException("Cannot close the only one account ID %s"
                    .formatted(id));
        }
        Account accountToDeposit = userAccounts.stream()
                .filter(a -> !Objects.equals(a.getId(), id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found account for deposit amount"));

        accountHibernateRepository.closeById(id);
        depositAmount(accountToDeposit.getId(), accountToClose.getMoneyAmount());
    }

    private void updateBalance(Long id, BigDecimal delta, boolean isWithdrawal) {
        Account account = getActiveAccount(id);
        if (account == null || (isWithdrawal && account.getMoneyAmount().compareTo(delta.abs()) < 0)) {
            throw new IllegalArgumentException("Cannot update balance account ID %s"
                    .formatted(id));
        }

        BigDecimal newAmount = account.getMoneyAmount().add(delta).setScale(2, RoundingMode.HALF_UP);
        accountHibernateRepository.saveAmount(id, newAmount);
    }
}
