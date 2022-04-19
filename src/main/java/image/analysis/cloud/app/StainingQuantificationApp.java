package image.analysis.cloud.app;

import image.analysis.cloud.app.entrypoint.gui.GuiApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class StainingQuantificationApp {

    static Logger logger = LoggerFactory.getLogger(StainingQuantificationApp.class);

    public static void main(String[] args) {
        //start web
        ConfigurableApplicationContext context = new SpringApplicationBuilder(StainingQuantificationApp.class)
                .headless(false)
                .run(args);
        //start gui
        GuiApp guiApp = context.getBean(GuiApp.class);
        guiApp.start();
    }

}
