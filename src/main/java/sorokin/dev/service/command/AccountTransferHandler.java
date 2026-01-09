package sorokin.dev.service.command;

import org.springframework.stereotype.Component;
import sorokin.dev.service.AccountService;

import java.math.BigDecimal;
import java.util.Scanner;

import static sorokin.dev.service.HelperUtils.verifyAndGetAccountId;
import static sorokin.dev.service.command.CommandType.ACCOUNT_TRANSFER;
import static sorokin.dev.service.HelperUtils.getAmount;

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
        Long sourceAccountId = verifyAndGetAccountId(sourceAccountIdValue);

        System.out.print("Enter target account ID:");
        System.out.print("> ");
        String targetAccountIdValue = scanner.nextLine();
        if (targetAccountIdValue.equals(sourceAccountIdValue)) {
        System.out.println("target account ID should not match source account ID\n" +
                    "return to enter one of operation...");
        return;
        }
        Long targetAccountId = verifyAndGetAccountId(targetAccountIdValue);

        System.out.print("Enter amount:");
        System.out.print("> ");
        String amountValue = scanner.nextLine();
        BigDecimal amount = getAmount(amountValue);
        if (amount == null) {
            System.out.println("amount value not valid, return to enter one of operation...");
            return;
        }
        accountService.transferAmount(sourceAccountId, targetAccountId, amount);
        System.out.printf("Amount %s transferred from account ID %s to account ID %s%n", amount, sourceAccountId, targetAccountId);
    }
}
