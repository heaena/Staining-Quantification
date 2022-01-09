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
        return getImgAnalysisPath() + "/input";
    }

    public static String getImgAnalysisOutputPath() {
        return getImgAnalysisPath() + "/output";
    }

    public static String getImgAnalysisPath() {
        return workspaceRootPath + "/image-analysis";
    }

    public static String getImgAnalysisH2Path() {
        return workspaceRootPath + "/h2";
    }

    public static String getWorkspaceRootPath() {
        return workspaceRootPath;
    }
}
