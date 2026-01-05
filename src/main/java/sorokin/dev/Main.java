package sorokin.dev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sorokin.dev.config.AppConfig;
import sorokin.dev.service.OperationConsoleListener;

public class Main {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);

        OperationConsoleListener operationConsoleListener = context.getBean(OperationConsoleListener.class);

        operationConsoleListener.start();

        context.close();
    }
}