package sorokin.dev.dto;

import java.math.BigDecimal;

/**
 * Счет.
 */
public class Account {

    /**
     * Идентификатор.
     */
    private Long id;

    /**
     * Идентификатор пользователя владельца счета.
     */
    private Long userId;

    /**
     * Текущий баланс счета в рублях.
     */
    private BigDecimal moneyAmount;

    /**
     * Счет закрыт.
     */
    private boolean isClosed = false;

    public Account(Long id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
