package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.dto.Account;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.service.command.CommandType.ACCOUNT_DEPOSIT;
import static sorokin.dev.service.HelperUtils.getAmount;
import static sorokin.dev.service.HelperUtils.getLongValue;
import static sorokin.dev.service.HelperUtils.isAccountIdValid;

@Component
public class AccountDepositHandler implements CommandHandler {

    private final AccountService accountService;

    public AccountDepositHandler(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    public CommandType getType() {
        return ACCOUNT_DEPOSIT;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter account ID:");
        System.out.print("> ");
        String accountIdValue = scanner.nextLine();
        var account = getAccount(accountIdValue);
        if (account == null) {
            System.out.println("account ID not exist, return to enter one of operation...");
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
        if (accountService.depositAmount(account.getId(), amount)) {
            System.out.println("Amount " + amount +" deposited to account ID:" + account.getId());
        } else {
            System.out.println("The operation failed, please try again later.");
        }
    }

    private Account getAccount(String value) {
        if (!isAccountIdValid(value)) {
            System.out.print("account ID not valid, return to enter one of operation...");
            return null;
        }
        Long accountId = getLongValue(value);
        if (accountId < 0 ) {
            System.out.print("account ID not valid, return to enter one of operation...");
            return null;
        }
        return accountService.getActiveAccount(accountId);
    }
}
