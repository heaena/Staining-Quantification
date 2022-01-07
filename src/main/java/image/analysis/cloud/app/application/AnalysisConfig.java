package image.analysis.cloud.app.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "image.analysis.cloud.app")
public class AnalysisConfig {
    private static String workspaceRootPath;

    @Value("${user.home}")
    public void setUserHome(String userHome) {
        workspaceRootPath = userHome + "/.image-analysis-cloud-app";
    }

    public static String getLogsPath() {
        return workspaceRootPath + "/logs";
    }

    public static String getImgAnalysisInputPath() {
        return workspaceRootPath + "/image-analysis/input";
    }

    public static String getImgAnalysisOutputPath() {
        return workspaceRootPath + "/image-analysis/output";
    }

    public static String getWorkspaceRootPath() {
        return workspaceRootPath;
    }
}
