package sorokin.dev.dto;

import java.math.BigDecimal;

/**
 * Счет.
 */
public class Account {

    /**
     * Идентификатор.
     */
    private final Long id;

    /**
     * Идентификатор пользователя владельца счета.
     */
    private final Long userId;

    /**
     * Текущий баланс счета в рублях.
     */
    private BigDecimal moneyAmount;

    /**
     * Счет закрыт.
     */
    private boolean isClosed = false;

    public Account(Long id, Long userId, BigDecimal moneyAmount) {
        this.id = id;
        this.userId = userId;
        this.moneyAmount = moneyAmount;
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", userId=" + userId +
                ", moneyAmount=" + moneyAmount +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public BigDecimal getMoneyAmount() {
        return moneyAmount;
    }

    public void setMoneyAmount(BigDecimal moneyAmount) {
        this.moneyAmount = moneyAmount;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }
}
