package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.service.AccountService;

import java.util.Scanner;

import static sorokin.dev.service.HelperUtils.verifyAndGetAccountId;
import static sorokin.dev.service.command.CommandType.ACCOUNT_CLOSE;

@Component
public class CloseAccountHandler implements CommandHandler {

    private final AccountService accountService;

    public CloseAccountHandler(AccountService accountService) {
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
        String accountIdValue = scanner.nextLine();
        Long accountId = verifyAndGetAccountId(accountIdValue);
        accountService.close(accountId);
        System.out.printf("Account %s closed%n", accountId);
    }
}
