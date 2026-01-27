package sorokin.dev.domain.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import sorokin.dev.domain.entity.Account;
import sorokin.dev.domain.entity.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class AccountHibernateRepository {

    private final static String FIND_QUERY =
            "SELECT a FROM Account a WHERE a.isClosed = false and a.user.id = :userId";

    private final SessionFactory sessionFactory;
    private final TransactionalHelper transactionalHelper;

    public AccountHibernateRepository(SessionFactory sessionFactory, TransactionalHelper transactionalHelper) {
        this.sessionFactory = sessionFactory;
        this.transactionalHelper = transactionalHelper;
    }

    public Account save(User user, BigDecimal amount) {
        return transactionalHelper.executeTransaction(session -> {
            Account newAccount = new Account();
            newAccount.setMoneyAmount(amount);
            newAccount.setUser(user);
            session.persist(newAccount);
            return newAccount;
        });
    }

    public Account findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(Account.class, id);
        }
    }


    public void saveAmount(Long id, BigDecimal amount) {
        Account account = findById(id);
        if (account != null) {
            account.setMoneyAmount(amount);
            transactionalHelper.executeTransaction(session -> {
                session.merge(account);
            });

        }
    }

    public void closeById(Long id) {
        Account account = findById(id);
        if (account != null) {
            account.setClosed(true);
            transactionalHelper.executeTransaction(session -> {
                session.merge(account);
            });
        }
    }

    public List<Account> findAllActiveByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(FIND_QUERY, Account.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }

    public void transferAmount(Account source, Account target,
                               BigDecimal amount, BigDecimal amountToDeposit) {
        source.setMoneyAmount(amount);
        target.setMoneyAmount(amountToDeposit);
        transactionalHelper.executeTransaction(session -> {
            session.merge(source);
            session.merge(target);
        });
    }
}
