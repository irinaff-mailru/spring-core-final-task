package sorokin.dev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:message.properties")
public class MessageConfig {

    @Value("${first.message}")
    private String firstMessage;

    public String getFirstMessage() {
        return firstMessage;
    }
}
