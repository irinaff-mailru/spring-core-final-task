package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.Account;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.service.HelperUtils.getAmount;
import static sorokin.dev.service.HelperUtils.getLongValue;
import static sorokin.dev.service.HelperUtils.isAccountIdValid;
import static sorokin.dev.service.command.CommandType.ACCOUNT_TRANSFER;
import static sorokin.dev.service.command.CommandType.ACCOUNT_WITHDRAW;

@Component
public class AccountWithdrawHandler implements CommandHandler {

    private final AccountService accountService;

    public AccountWithdrawHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public CommandType getType() {
        return ACCOUNT_WITHDRAW;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter  account ID:");
        System.out.print("> ");
        String accountIdValue = scanner.nextLine();
        var account = getAccount(accountIdValue);
        if (account == null) {
            System.out.println("account ID not exist, return to enter one of operation...");
            return;
        }

        System.out.print("Enter amount to withdraw:");
        System.out.print("> ");
        String amountValue = scanner.nextLine();
        BigDecimal amount = getAmount(amountValue);
        if (amount == null) {
            System.out.println("amount value not valid, return to enter one of operation...");
            return;
        }
        if (accountService.withdraw(account.getId(), amount)) {
            System.out.println("Amount " + amount +" withdrawn from account ID " + account.getId());
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
