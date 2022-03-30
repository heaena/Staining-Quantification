package image.analysis.cloud.app.application.service;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisTask;
import image.analysis.cloud.app.application.domain.repo.AnalysisTaskRepo;
import image.analysis.cloud.app.application.domain.repo.FileSystemRepo;
import image.analysis.cloud.app.infra.ResponseWrapper;
import image.analysis.cloud.app.infra.rpc.RemoteAnalysisPlatformService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class SourceImageService {

    // 源图片的目录
    private static final String sourceImagePathName = "/analysis-file";

    @Autowired
    private AnalysisTaskService analysisTaskService;

    /**
     * 获取源图片
     * @param parentPath
     * @param name
     * @return
     * @throws IOException
     */
    public List<FileSystem> listSourceImage(String parentPath, String name) throws IOException {
        File[] files = listChildFile(getRootPath() + "/" + parentPath, name);
        List<FileSystem> res = new LinkedList<>();
        if (files != null) {
            for (File file: files) {
                FileSystem fileSystem = new FileSystem();
                fileSystem.setName(file.getName());
                fileSystem.setPath(file.getCanonicalPath().replace(getRootPath(), ""));
                fileSystem.setCanonicalFilePath(file.getCanonicalPath());
                fileSystem.setLastModified(file.lastModified());
                if (file.isDirectory()) {
                    fileSystem.setDir(true);
                } else {
                    //如果是图片，获取图片访问路径
                    fileSystem.setResourcePath(getSourceImageResourcePath(file));
                }
                res.add(fileSystem);
            }
        }
        return res;
    }

    /**
     * 获取图片访问路径
     * @param file
     * @return
     * @throws IOException
     */
    private String getSourceImageResourcePath(File file) throws IOException {
        return file.getCanonicalPath().replace(AnalysisConfig.getImgAnalysisWorkspacePath(), "");
    }

    /**
     * 获取子文件
     * @param path
     * @param filterName
     * @return
     */
    private File[] listChildFile(String path, String filterName) {
        File parentDir = new File(path);
        return parentDir.listFiles(file -> {
            if (file.isHidden()) {
                return false;
            }
            if (StringUtils.isNotEmpty(filterName)) {
                return file.getName().matches(".*" + filterName + ".*");
            }
            return true;
        });
    }

    /**
     * 删除文件
     * @param path
     * @throws Exception
     */
    public void removeFile(String path) throws Exception {
        File file = new File(getRootPath() + "/" + path);
        removeCurrentAndChild(file);
    }

    /**
     * 获取根目录
     * @return
     */
    private String getRootPath() {
        return AnalysisConfig.getImgAnalysisWorkspacePath() + sourceImagePathName;
    }

    /**
     * 删除子文件
     * @param file
     * @throws Exception
     */
    private void removeCurrentAndChild(File file) {

        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File innerFile : files) {
                    removeCurrentAndChild(innerFile);
                }
            }
            file.delete();
        }
    }

    /**
     * 创建分析任务
     * @param taskName 任务名称
     * @param folderPath 文件夹
     * @param imageList 图片名称
     * @param param
     */
    public void createTask(String taskName, String folderPath, List<String> imageList, String param) {
        List<String> imageCanonicalFilePathList = getImageCanonicalFilePath(folderPath, imageList);
        analysisTaskService.createAnalysisTask(taskName, imageCanonicalFilePathList, param);
    }

    private List<String> getImageCanonicalFilePath(String folderPath, List<String> imageList) {
        return imageList.stream().map(imageName -> getRootPath() + "/" + folderPath + "/" + imageName).toList();
    }
}
