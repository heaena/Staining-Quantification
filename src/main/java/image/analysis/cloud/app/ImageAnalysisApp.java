package image.analysis.cloud.app;

import image.analysis.cloud.app.entrypoint.gui.GuiApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class ImageAnalysisApp {

    static Logger logger = LoggerFactory.getLogger(ImageAnalysisApp.class);

    public static void main(String[] args) {
        //start web
//        System.getProperties().put( "server.port", ServerPortUtil.getAvailablePort());
        System.getProperties().put( "server.port", "28084");
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ImageAnalysisApp.class)
                .headless(false)
                .run(args);
        //start gui
        GuiApp guiApp = context.getBean(GuiApp.class);
        guiApp.start();
    }

}
