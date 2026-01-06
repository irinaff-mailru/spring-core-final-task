package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.config.AccountConfig;
import sorokin.dev.dto.Account;
import sorokin.dev.dto.CommandType;
import sorokin.dev.service.AccountService;

import java.util.Scanner;

import static sorokin.dev.dto.CommandType.ACCOUNT_CLOSE;
import static sorokin.dev.service.HelperUtils.getLongValue;
import static sorokin.dev.service.HelperUtils.isAccountIdValid;

@Component
public class CloseAccountHandler implements CommandHandler {

    private final AccountService accountService;

    public CloseAccountHandler(AccountService accountService, AccountConfig accountConfig) {
        this.accountService = accountService;
    }

    @Override
    public CommandType getType() {
        return ACCOUNT_CLOSE;
    }

    @Override
    public void handle(Scanner scanner) {
        System.out.print("Enter the account id for closing:");
        System.out.print("> ");
        System.out.print("Enter source account ID:");
        System.out.print("> ");
        String accountIdValue = scanner.nextLine();
        var account = getAccount(accountIdValue);
        if (account == null) {
            System.out.println("account ID not exist or already closed, return to enter one of operation...");
            return;
        }
        if (accountService.close(account.getId())) {
            System.out.println("Account " + account.getId() +" closed");
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
