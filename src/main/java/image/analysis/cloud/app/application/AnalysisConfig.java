package image.analysis.cloud.app.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "image.analysis.cloud.app")
public class AnalysisConfig {
    private static String workspaceRootPath;
    private static String workspaceName = ".image-analysis-cloud-app";
    private static String inputRootPath = "/input";
    private static String outputRootPath = "/output";

    @Value("${user.home}")
    public void setUserHome(String userHome) {
        workspaceRootPath = userHome + "/" + workspaceName;
    }



    public static String getLogsPath() {
        return workspaceRootPath + "/logs";
    }

    public static String getImgAnalysisInputPath() {
        return getImgAnalysisPath() + getInputRootPath();
    }

    public static String getImgAnalysisOutputPath() {
        return getImgAnalysisPath() + getOutputRootPath();
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

    public static String getInputRootPath() {
        return inputRootPath;
    }

    public static String getOutputRootPath() {
        return outputRootPath;
    }
}
