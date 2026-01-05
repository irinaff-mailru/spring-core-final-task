package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.CommandType;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Scanner;

import static sorokin.dev.dto.CommandType.ACCOUNT_DEPOSIT;

@Component
public class AccountDepositHandler implements CommandHandler {

    private final AccountService accountService;

    public AccountDepositHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter account ID:");
        System.out.print("> ");
        String accountIdValue = scanner.nextLine();
        if (!isAccountIdValid(accountIdValue)) {
            System.out.print("account ID not valid, return to enter one of operation...");
            return;
        }
        Long accountId = getLongValue(accountIdValue);
        var optionalAccount = accountService.findAccountById(accountId);
        if (accountId < 0 || optionalAccount.isEmpty()) {
            System.out.print("account ID not exist, return to enter one of operation...");
            return;
        }

        System.out.print("Enter amount:");
        System.out.print("> ");
        String amountValue = scanner.nextLine();

        if (!isAmountValid(amountValue)) {
            System.out.print("amount value not valid, return to enter one of operation...");
            return;
        }
        BigDecimal amount = parseAmount(amountValue);
        accountService.addAmount(accountId, amount);
        System.out.println("Amount " + amount +" deposited to account ID:" + accountId);
    }

    @Override
    public CommandType getType() {
        return ACCOUNT_DEPOSIT;
    }

    private boolean isAccountIdValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.length() < 19 && value.matches("\\d+");
    }

    private Long getLongValue(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            System.out.print("userId not valid, return to enter one of operation...");
            return -1L;
        }
    }

    private boolean isAmountValid(String value) {
        if (value == null || value.isBlank()) {
            return false;
        }
        return value.length() < 19 && value.matches("\\d+([.,]\\d+)?$");
    }

    private BigDecimal parseAmount(String value) {
        String sanitizedValue = value.replace(',', '.');
        BigDecimal amount = new BigDecimal(sanitizedValue);
        return amount.setScale(2, RoundingMode.HALF_UP);
    }
}
