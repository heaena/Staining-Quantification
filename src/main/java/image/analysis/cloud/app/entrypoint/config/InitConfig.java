package image.analysis.cloud.app.entrypoint.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class InitConfig implements ApplicationContextInitializer {
    private static String ROOT_PATH = null;

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
        ConfigurableEnvironment environment = configurableApplicationContext.getEnvironment();
        ROOT_PATH = environment.getProperty("web.file-root");
    }

    public static String getRootFilePath() {
        return ROOT_PATH;
    }


}
