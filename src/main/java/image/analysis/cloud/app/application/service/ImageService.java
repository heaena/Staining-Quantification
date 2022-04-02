package image.analysis.cloud.app.application.service;

import image.analysis.cloud.app.application.AnalysisConfig;

import java.io.File;
import java.io.IOException;

public interface ImageService {
    /**
     * 获取文件访问路径
     *
     * @param file
     * @return
     * @throws IOException
     */
    default String getResourcePath(File file) throws IOException {
        return checkFilePath(file.getCanonicalPath()).replace(AnalysisConfig.getImgAnalysisWorkspacePath(), "");
    }

    default String checkFilePath(String path) {
        return path.replaceAll("\\\\", "/");
    }
}
