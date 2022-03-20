package image.analysis.cloud.app.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;

@Component
@ConfigurationProperties(prefix = "image.analysis.cloud.app")
public class AnalysisConfig {
    private static String workspaceRootPath;
    private static String workspaceName = ".image-analysis-cloud-app";
    private static String analysisWorkspaceName = "image-analysis";
    private static String testImagePath = "";

    static {
        File inputFile = null;
        try {
            inputFile = ResourceUtils.getFile("/rcode/test.jpg");
//            inputFile = new ClassPathResource("/rcode/test.jpg").getFile();
            testImagePath = inputFile.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
    }

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

    public static String getTestRscriptOutputPath() {
        return workspaceRootPath + "/test";
    }

    public static String getTestImagePath() {
        return testImagePath;
    }

}
