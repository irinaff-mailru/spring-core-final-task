package sorokin.dev.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Утилитный класс с проверками вводимых данных
 */
public final class HelperUtils {

    private HelperUtils() {
        throw new UnsupportedOperationException("This is utility class");
    }

    public static boolean isUserIdValid(String value) {
        return (value != null)
                && !value.isBlank()
                && value.length() < 19
                && value.matches("\\d+");
    }

    public static boolean isLoginValid(String value) {
        return (value != null)
                && !value.isBlank()
                && value.length() > 2
                && value.length() < 51;
    }

    public static Long getLongValue(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return -1L;
        }
    }

    public static boolean isAccountIdValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.length() < 19 && value.matches("\\d+");
    }

    public static BigDecimal getAmount(String value) {
        if (!isAmountValid(value)) {
            return null;
        }
        BigDecimal amount = parseAmount(value);
        if (amount.compareTo(new BigDecimal("0.01")) < 0) {
            return null;
        }
        return amount;
    }

    public static boolean isAmountValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.length() < 19 && value.matches("\\d+([.,]\\d+)?$");
    }

    public static BigDecimal parseAmount(String value) {
        String sanitizedValue = value.replace(',', '.');
        BigDecimal amount = new BigDecimal(sanitizedValue);
        return amount.setScale(2, RoundingMode.HALF_UP);
    }

    public static Long verifyAndGetAccountId(String value) {
        if (!isAccountIdValid(value)) {
            System.out.print("account ID not valid, return to enter one of operation...");
            return null;
        }
        Long accountId = getLongValue(value);
        if (accountId < 0) {
            System.out.print("account ID not valid, return to enter one of operation...");
            return null;
        }
        return accountId;
    }
}
