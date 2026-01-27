package sorokin.dev.domain.repository;

import config.HibernateTestConfiguration;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sorokin.dev.config.AccountProperties;
import sorokin.dev.domain.entity.Account;
import sorokin.dev.domain.entity.User;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        HibernateTestConfiguration.class,
        AccountProperties.class
})
class AccountHibernateRepositoryTest {

    @Autowired
    private AccountHibernateRepository repository;

    @Autowired
    private AccountProperties properties;

    @Autowired
    private SessionFactory sessionFactory;

    private Long userId_1;
    private Long userId_2;
    private Long sourceId;
    private Long targetId;

    @BeforeEach
    void setUp() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            User user1 = new User();
            user1.setLogin("testUser1_" + System.currentTimeMillis());
            session.persist(user1);

            User user2 = new User();
            user2.setLogin("testUser2_" + System.currentTimeMillis());
            session.persist(user2);

            Account source = new Account();
            source.setUser(user1);
            source.setMoneyAmount(new BigDecimal("100.00"));
            session.persist(source);

            Account target = new Account();
            target.setMoneyAmount(new BigDecimal("50.00"));
            target.setUser(user2);
            session.persist(target);

            tx.commit();
            userId_1 = user1.getId();
            userId_2 = user2.getId();
            sourceId = source.getId();
            targetId = target.getId();
        }
    }
    @AfterEach
    void teardown() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("DELETE FROM Account").executeUpdate();
            session.createQuery("DELETE FROM User").executeUpdate();
            tx.commit();
        }
    }

    @Test
    void save() {
        //Given
        User user;
        try (Session session = sessionFactory.openSession()) {
            user = session.find(User.class, userId_1);
        }
        //When
        Account account = repository.save(user, new BigDecimal("150.00"));

        //Then
        assertNotNull(account.getId());
        Account found = repository.findById(account.getId());
        assertNotNull(found);
        assertEquals(0, new BigDecimal("150.00").compareTo(found.getMoneyAmount()));
    }

    //TODO реализовать в следующем задании
    @Test
    void findById() {
    }

    //TODO реализовать в следующем задании
    @Test
    void saveAmount() {
    }

    //TODO реализовать в следующем задании
    @Test
    void closeById() {
    }

    @Test
    void transferAmount() {
        //GIVEN
        Account source;
        Account target;
        try (Session session = sessionFactory.openSession()) {
            source = session.get(Account.class, sourceId);
            target = session.get(Account.class, targetId);
        }

        BigDecimal newSourceAmount = new BigDecimal("90.00");
        BigDecimal newTargetAmount = new BigDecimal("60.00");

        //WHEN
        repository.transferAmount(source, target, newSourceAmount, newTargetAmount);

        //THEN
        try (Session session = sessionFactory.openSession()) {
            Account updatedSource = session.get(Account.class, sourceId);
            Account updatedTarget = session.get(Account.class, targetId);

            assertEquals(0, newSourceAmount.compareTo(updatedSource.getMoneyAmount()));
            assertEquals(0, newTargetAmount.compareTo(updatedTarget.getMoneyAmount()));
        }
    }

    @Configuration
    @Import({AccountHibernateRepository.class, TransactionalHelper.class})
    static class TestConfig {}
}