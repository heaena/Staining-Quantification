package image.analysis.cloud.app.application;

import image.analysis.cloud.app.application.service.AnalysisTaskService;
import image.analysis.cloud.app.application.service.SourceImageService;
import image.analysis.cloud.app.infra.util.ServerPortUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Configuration
@Component
@ConfigurationProperties(prefix = "image.analysis.cloud.app")
@AutoConfigureBefore(ServerProperties.class)
@Slf4j
public class AnalysisConfig {

    private Boolean availablePort = false;

    private static String workspaceRootPath;
    private static String workspaceName = "/staining-quantification-app";
    private static String analysisWorkspaceName = "/staining-quantification";
    private static File imgAnalysisWorkspaceFile;
    private static String testImagePath = "";
    private static String rscript = "Rscript";

    static {
        File inputFile = null;
        try {
            inputFile = ResourceUtils.getFile("/rcode/test.jpg");
            testImagePath = inputFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

    @Value("${user.home}")
    public void setUserHome(String userHome) {
        workspaceRootPath = userHome + workspaceName;
        String imgAnalysisWorkspaceFilePath = workspaceRootPath + analysisWorkspaceName;
        imgAnalysisWorkspaceFile = new File(imgAnalysisWorkspaceFilePath);
        if (!imgAnalysisWorkspaceFile.exists()) {
            imgAnalysisWorkspaceFile.mkdirs();
        }

        SourceImageService.sourceImageRootFolder = new File(imgAnalysisWorkspaceFilePath  + SourceImageService.sourceImagePathName);
        if(!SourceImageService.sourceImageRootFolder.exists()) {
            SourceImageService.sourceImageRootFolder.mkdirs();
        }

        AnalysisTaskService.outputRootFolder = new File(imgAnalysisWorkspaceFilePath + AnalysisTaskService.outputRoot);
        if(!AnalysisTaskService.outputRootFolder.exists()) {
            AnalysisTaskService.outputRootFolder.mkdirs();
        }
    }

    @Bean
    public TomcatServletWebServerFactory servletContainer() {
        if (availablePort != null && availablePort) {
            return new TomcatServletWebServerFactory(ServerPortUtil.getAvailablePort());
        } else {
            return new TomcatServletWebServerFactory();
        }

    }

    public void setAvailablePort(Boolean availablePort) {
        this.availablePort = availablePort;
    }

    public void setRscript(String rscript) throws IOException {
        if (rscript != null) {
            if ("inner".equals(rscript)) {
                String path = new ClassPathResource("/rcode/").getFile().getParentFile().getParentFile().getCanonicalPath() + "/R/bin/Rscript";
                path = new File(path).getCanonicalPath();
                log.info("Rscript path=" + path);
                AnalysisConfig.rscript = path;
            } else {
                AnalysisConfig.rscript = rscript;
            }
        }
    }

    public static String getLogsPath() {
        return workspaceRootPath + "/logs";
    }

    public static String getImgAnalysisWorkspacePath() {
        try {
            return imgAnalysisWorkspaceFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getImgAnalysisH2Path() {
        return workspaceRootPath + "/h2/h2";
    }

    public static String getWorkspaceRootPath() {
        return workspaceRootPath;
    }

    public static String getTestRscriptOutputPath() {
        return workspaceRootPath + "/test";
    }

    public static String getTestImagePath() {
        return testImagePath;
    }

    public static String getRscript() {
        return rscript;
    }
}
