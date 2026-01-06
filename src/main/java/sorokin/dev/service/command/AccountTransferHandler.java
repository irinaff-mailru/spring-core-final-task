package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.CommandType;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.dto.CommandType.ACCOUNT_TRANSFER;
import static sorokin.dev.service.HelperUtils.getAmount;
import static sorokin.dev.service.HelperUtils.getLongValue;
import static sorokin.dev.service.HelperUtils.isAccountIdValid;

@Component
public class AccountTransferHandler implements CommandHandler {

    private final AccountService accountService;

    public AccountTransferHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public CommandType getType() {
        return ACCOUNT_TRANSFER;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter source account ID:");
        System.out.print("> ");
        String sourceAccountIdValue = scanner.nextLine();
        var sourceAccount = getAccount(sourceAccountIdValue);
        if (sourceAccount == null) {
            System.out.println("source account ID not exist, return to enter one of operation...");
            return;
        }

        System.out.print("Enter target account ID:");
        System.out.print("> ");
        String targetAccountIdValue = scanner.nextLine();
        var targetAccount = getAccount(targetAccountIdValue);
        if (targetAccount == null) {
            System.out.println("target account ID not exist, return to enter one of operation...");
            return;
        }

        System.out.print("Enter amount:");
        System.out.print("> ");
        String amountValue = scanner.nextLine();
        BigDecimal amount = getAmount(amountValue);
        if (amount == null) {
            System.out.println("amount value not valid, return to enter one of operation...");
            return;
        }
        if (accountService.transferAmount(sourceAccount.getId(), targetAccount.getId(), amount)) {
            System.out.println("Amount " + amount +" transferred from account ID " + sourceAccount.getId() +
                    " to account ID " + targetAccount.getId());
        } else {
            System.out.println("The operation failed, please try again later.");
        }

    }

    private Account getAccount(String value) {
        if (!isAccountIdValid(value)) {
            System.out.println("account ID not valid, return to enter one of operation...");
            return null;
        }
        Long accountId = getLongValue(value);
        if (accountId < 0 ) {
            System.out.println("account ID not valid, return to enter one of operation...");
            return null;
        }
        return accountService.getActiveAccount(accountId);
    }
}
