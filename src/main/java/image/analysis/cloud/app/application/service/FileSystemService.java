package image.analysis.cloud.app.application.service;

import au.com.bytecode.opencsv.CSVReader;
import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisResult;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisTask;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
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
            ImageAnalysisTask imageAnalysisTask = new ImageAnalysisTask(taskId, taskName, imageCanonicalPath, outputFolderPath, param);
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
        return file.getCanonicalPath().replace(AnalysisConfig.getImgAnalysisWorkspacePath(), "");
    }

    private String getOutputResourcePath(File file) throws IOException {
        return file.getCanonicalPath().replace(AnalysisConfig.getImgAnalysisWorkspacePath(), "");
    }

    public ResponseWrapper addFile(MultipartFile[] uploadFiles, String folderName) throws IOException {
        if (StringUtils.isEmpty(folderName)) {
            folderName = "";
        }
        String uploadPath = getRootPath() + "/" + folderName;
        for (MultipartFile uploadFile:uploadFiles) {
            File file = new File(uploadPath, uploadFile.getOriginalFilename());
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

            //输出的文件
            for (File outputFileItem: outputFileList) {
                String fileName = outputFileItem.getName();
                ImageAnalysisResult.OutputFile outputFile = new ImageAnalysisResult.OutputFile();
                outputFile.setName(fileName);
                outputFile.setCanonicalPath(outputFileItem.getCanonicalPath());
                outputFile.setResourcePath(getOutputResourcePath(outputFileItem));
                if (fileName.startsWith("stained")) {
                    outputItem.putOutputFile("stained", outputFile);
                } else if (fileName.startsWith("total")) {
                    outputItem.putOutputFile("total", outputFile);
                } else if (fileName.startsWith("data")) {
                    outputItem.putOutputFile("data", outputFile);
                    outputFile.setData(JSONObject.toJSONString(parseCsv(outputFileItem)));
                } else if (fileName.startsWith("log")) {
                    outputItem.putOutputFile("log", outputFile);
                    outputFile.setData(JSONObject.toJSONString(parseCsv(outputFileItem)));
                }

            }


        }
        return result;
    }

    private String getImageAnalysisOutputRootFolderPath(String folderName, String imageName) {
        return getCanonicalPathByFolder(folderName) + "/" + imageName + outputPathName;
    }

    private List<String []> parseCsv(File file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        return new CSVReader(new InputStreamReader(dataInputStream,"UTF-8")).readAll();
    }

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

    public void removeFile(String path) throws Exception {
        File file = new File(getRootPath() + "/" + path);
        removeCurrentAndChild(file);
    }

    public static void removeCurrentAndChild(File file) throws Exception {

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
}
