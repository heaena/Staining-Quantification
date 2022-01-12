package image.analysis.cloud.app.application.service;

import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisResult;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisTask;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileSystemService {

    private static final String folderPathName = "/analysis-file";
    private static final String outputPathName = "/output";

    public List<FileSystem> listFolder(String name) throws IOException {
        File[] files = listChildFolder(getRootPath(), name);
        List<FileSystem> res = new LinkedList<>();
        if (files != null) {
            for (File file: files) {
                FileSystem fileSystem = new FileSystem();
                fileSystem.setName(file.getName());
                fileSystem.setCanonicalFilePath(file.getCanonicalPath());
                fileSystem.setLastModified(file.lastModified());
                res.add(fileSystem);
            }
        }
        return res;
    }

    public List<ImageAnalysisTask> getAnalysisTask(String taskName, String folderName, List<String> imageList, String param) {
        long taskId = getTaskId();

        List<ImageAnalysisTask> tasks = new ArrayList<>();
        for (String imageName: imageList) {
            String imageCanonicalPath = getSourceImageCanonicalPath(folderName, imageName);
            String outputFolderPath = getImageAnalysisOutputFolderPath(taskId, taskName, folderName, imageName);
            ImageAnalysisTask imageAnalysisTask = new ImageAnalysisTask(taskId, taskName, imageCanonicalPath, outputFolderPath);
            tasks.add(imageAnalysisTask);
        }
        return tasks;
    }

    private long getTaskId() {
        return System.currentTimeMillis();
    }

    public String getImageAnalysisOutputFolderPath(long taskId, String taskName, String folderName, String imageName) {
        return getImageAnalysisOutputRootFolderPath(folderName, imageName) + "/" + getImageAnalysisOutputFolderName(taskId, taskName);
    }

    private String getImageAnalysisOutputFolderName(long taskId, String taskName) {
        return "" + taskId + "-" + taskName;
    }

    private File[] listChildFolder(String path, String filterName) {
        File parentDir = new File(path);
        return parentDir.listFiles(file -> {
            if (file.isHidden() || file.isFile()) {
                return false;
            }
            if (StringUtils.isNotEmpty(filterName)) {
                return file.getName().matches(".*" + filterName + ".*");
            }
            return true;
        });
    }

    public List<FileSystem> listImage(String folderName, String imageName) throws IOException {
        File[] files = listChildFolder(getRootPath() + "/" + folderName, imageName);
        List<FileSystem> res = new LinkedList<>();
        if (files != null) {
            for (File file: files) {
                FileSystem fileSystem = new FileSystem();
                fileSystem.setName(file.getName());
                fileSystem.setCanonicalFilePath(getSourceImageCanonicalPath(file));
                fileSystem.setResourcePath(getSourceImageResourcePath(file));
                fileSystem.setLastModified(file.lastModified());
                res.add(fileSystem);
            }
        }
        return res;
    }

    private String getCanonicalPathByFolder(String folderName) {
        return getRootPath() + "/" + folderName;
    }

    private String getSourceImageCanonicalPath(File file) throws IOException {
        return file.getCanonicalPath() + "/" + file.getName();
    }

    private String getRootPath() {
        return AnalysisConfig.getImgAnalysisWorkspacePath() + folderPathName;
    }

    private String getSourceImageCanonicalPath(String folderName, String imageName) {
        return getCanonicalPathByFolder(folderName) + "/" + imageName + "/" + imageName;
    }

    private String getSourceImageResourcePath(File file) throws IOException {
        return file.getCanonicalPath().replace(AnalysisConfig.getImgAnalysisWorkspacePath(), "") + "/" + file.getName();
    }

    private String getOutputResourcePath(File file) throws IOException {
        return file.getCanonicalPath().replace(AnalysisConfig.getImgAnalysisWorkspacePath(), "");
    }

    public ResponseWrapper addFile(MultipartFile[] uploadFiles, String folderName) throws IOException {
        String parentCanonicalPath = getRootPath() + "/" + folderName;
        for (MultipartFile uploadFile:uploadFiles) {
            String savePath = parentCanonicalPath + "/" + uploadFile.getOriginalFilename();
            File file = new File(savePath, uploadFile.getOriginalFilename());
            file.mkdirs();
            uploadFile.transferTo(file);
        }
        return ResponseWrapper.success();
    }

    /**
     * 新增文件夹
     * @param name 文件夹名称
     */
    public void addFolder(String name) {
        String folderPath = getRootPath() + "/" + name;
        File file = new File(folderPath);
        file.mkdirs();
    }

    public void removeImage(String folderName, String name) {
        String canonicalPath = getImageCanonicalPath(folderName, name);
        File file = new File(canonicalPath);
        file.delete();
    }

    private String getImageCanonicalPath(String folderName, String imageName) {
        return getFolderCanonicalPath(folderName) + "/" + imageName;
    }

    private String getFolderCanonicalPath(String folderName) {
        return getRootPath() + "/" + folderName;
    }
    public void removeFolder(String folderName) {
        String canonicalPath = getFolderCanonicalPath(folderName);
        File file = new File(canonicalPath);
        file.delete();
    }

    public ImageAnalysisResult getAnalysisResult(String folderName, String imageName) throws IOException {
        ImageAnalysisResult result = new ImageAnalysisResult();

        String outputPath = getImageAnalysisOutputRootFolderPath(folderName, imageName);
        result.setOutputPath(outputPath);
        String imageCanonicalPath = getImageCanonicalPath(folderName, imageName);
        result.setImageCanonicalPath(imageCanonicalPath);
        String sourceImageResourcePath = getSourceImageResourcePath(new File(imageCanonicalPath));
        result.setImageResourcePath(sourceImageResourcePath);

        File outputRootFile = new File(outputPath);

        result.setFolderName(folderName);
        result.setImageName(imageName);
        File[] outputTaskList = outputRootFile.listFiles(file -> {
            if (file.isHidden()) {
                return false;
            }
            if (file.isFile()) {
                return false;
            }
            return true;
        });
        for (File outputTask: outputTaskList) {
            ImageAnalysisResult.OutputItem outputItem = new ImageAnalysisResult.OutputItem();
            result.addOutputItem(outputItem);
            outputItem.setTaskName(outputTask.getName());
            File[] outputFileList = outputTask.listFiles(file -> {
                if (file.isHidden()) {
                    return false;
                }
                if (file.isDirectory()) {
                    return false;
                }
                return true;
            });

            for (File outputFileItem: outputFileList) {
                ImageAnalysisResult.OutputFile outputFile = new ImageAnalysisResult.OutputFile();
                outputFile.setName(outputFileItem.getName());
                outputFile.setCanonicalPath(outputFileItem.getCanonicalPath());
                outputFile.setResourcePath(getOutputResourcePath(outputFileItem));
                outputItem.addOutputFile(outputFile);
            }


        }
        return result;
    }

    private String getImageAnalysisOutputRootFolderPath(String folderName, String imageName) {
        return getCanonicalPathByFolder(folderName) + "/" + imageName + outputPathName;
    }
}
