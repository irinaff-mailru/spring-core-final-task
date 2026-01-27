package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.service.HelperUtils.getAmount;
import static sorokin.dev.service.HelperUtils.verifyAndGetAccountId;
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
        Long accountId = verifyAndGetAccountId(accountIdValue);

        System.out.print("Enter amount to withdraw:");
        System.out.print("> ");
        String amountValue = scanner.nextLine();
        BigDecimal amount = getAmount(amountValue);
        if (amount == null) {
            System.out.println("amount value not valid, return to enter one of operation...");
            return;
        }
        accountService.withdraw(accountId, amount);
        System.out.printf("Amount %s withdrawn from account ID %s%n", amount, accountId);
    }
}
