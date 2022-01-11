package image.analysis.cloud.app.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "image.analysis.cloud.app")
public class AnalysisConfig {
    private static String workspaceRootPath;
    private static String workspaceName = ".image-analysis-cloud-app";
    private static String analysisWorkspaceName = "image-analysis";

    @Value("${user.home}")
    public void setUserHome(String userHome) {
        workspaceRootPath = userHome + "/" + workspaceName;
    }



    public static String getLogsPath() {
        return workspaceRootPath + "/logs";
    }

    public static String getImgAnalysisWorkspacePath() {
        return workspaceRootPath + "/image-analysis";
    }

    public static String getImgAnalysisH2Path() {
        return workspaceRootPath + "/h2/h2";
    }

    public static String getWorkspaceRootPath() {
        return workspaceRootPath;
    }

}
