package sorokin.dev;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import sorokin.dev.config.AppConfig;

public class App {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(AppConfig.class);
    }
}