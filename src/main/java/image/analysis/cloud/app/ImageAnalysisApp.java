package image.analysis.cloud.app;

import image.analysis.cloud.app.entrypoint.gui.GuiApp;
import image.analysis.cloud.app.infra.util.ServerPortUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ImageAnalysisApp {

    public static void main(String[] args) {
        //start web
        System.getProperties().put( "server.port", ServerPortUtil.getAvailablePort());
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ImageAnalysisApp.class)
                .headless(false)
                .run(args);
        //start gui
        GuiApp guiApp = context.getBean(GuiApp.class);
        guiApp.start();
    }

}
