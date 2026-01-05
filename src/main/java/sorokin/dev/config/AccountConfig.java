package sorokin.dev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@PropertySource("classpath:application.properties")
public class AccountConfig {

    @Value("${account.default-amount}")
    private BigDecimal defaultAmount;

    public BigDecimal getDefaultAmount() {
        return defaultAmount;
    }
}
