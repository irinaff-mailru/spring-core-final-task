package sorokin.dev.domain.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.domain.entity.Account;
import sorokin.dev.domain.entity.User;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class UserHibernateRepository {

    private final static String FIND_BY_LOGIN_QUERY = "SELECT u FROM User u WHERE u.login = :login";
    private final static String FIND_ALL_USER_QUERY =
            """
                    SELECT DISTINCT u FROM User u
                    JOIN FETCH u.accounts a
                    WHERE a.isClosed = false
                    """;

    private final SessionFactory sessionFactory;
    private final TransactionalHelper transactionalHelper;
    private final AccountProperties accountProperties;

    public UserHibernateRepository(SessionFactory sessionFactory, TransactionalHelper transactionalHelper, AccountProperties accountProperties) {
        this.sessionFactory = sessionFactory;
        this.transactionalHelper = transactionalHelper;
        this.accountProperties = accountProperties;
    }

    public User save(String login, BigDecimal amount) {
        return transactionalHelper.executeTransaction(session -> {
            Account newAccount = new Account();
            newAccount.setMoneyAmount(amount);

            User newUser = new User();
            newUser.setLogin(login);
            newUser.addAccount(newAccount);
            session.persist(newUser);
            return newUser;
        });
    }

    public User findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.find(User.class, id);
        }
    }

    public User findByLogin(String login) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(FIND_BY_LOGIN_QUERY, User.class)
                    .setParameter("login", login)
                    .uniqueResult();
        }
    }

    public List<User> findUsersWithActiveAccounts() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(FIND_ALL_USER_QUERY, User.class)
                    .list();
        }
    }
}
