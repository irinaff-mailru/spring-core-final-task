package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.service.HelperUtils.verifyAndGetAccountId;
import static sorokin.dev.service.command.CommandType.ACCOUNT_DEPOSIT;
import static sorokin.dev.service.HelperUtils.getAmount;

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
        Long accountId = verifyAndGetAccountId(accountIdValue);

        System.out.print("Enter amount:");
        System.out.print("> ");
        String amountValue = scanner.nextLine();
        BigDecimal amount = getAmount(amountValue);
        if (amount == null) {
            System.out.println("amount value not valid, return to enter one of operation...");
            return;
        }
        accountService.depositAmount(accountId, amount);
        System.out.println("Amount %d deposited to account ID: %s".formatted(amount, accountId));
    }
}
