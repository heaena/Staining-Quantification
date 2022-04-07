package image.analysis.cloud.app.application.service;

import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.entrypoint.web.TaskRequestParam;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 源图片管理
 */
@Service
public class SourceImageService implements ImageService {

    // 源图片的目录
    public static final String sourceImagePathName = "/analysis-file";
    public static File sourceImageRootFolder;

    @Autowired
    private AnalysisTaskService analysisTaskService;

    /**
     * 获取源图片
     *
     * @param parentPath
     * @param name
     * @return
     * @throws IOException
     */
    public List<FileSystem> listSourceImage(String parentPath, String name) throws IOException {
        File[] files = listChildFile(getRootPath() + "/" + parentPath, name);
        List<FileSystem> res = new LinkedList<>();
        if (files != null) {
            for (File file : files) {
                FileSystem fileSystem = new FileSystem();
                fileSystem.setName(file.getName());
                fileSystem.setPath(file.getCanonicalPath().replace(getRootPath(), ""));
                fileSystem.setCanonicalFilePath(file.getCanonicalPath());
                fileSystem.setLastModified(file.lastModified());
                if (file.isDirectory()) {
                    fileSystem.setDir(true);
                } else {
                    //如果是图片，获取图片访问路径
                    fileSystem.setResourcePath(getResourcePath(file));
                }
                res.add(fileSystem);
            }
        }
        return res;
    }

    /**
     * 新增文件夹
     *
     * @param name 文件夹名称
     */
    public void addFolder(String name) throws IOException {
        String folderPath = getRootPath() + "/" + name;
        File file = new File(folderPath);
        file.mkdirs();
    }

    public ResponseWrapper addFile(MultipartFile[] uploadFiles, String parentPath) throws IOException {
        if (StringUtils.isEmpty(parentPath)) {
            parentPath = "";
        }
        String uploadPath = getRootPath() + "/" + parentPath;
        for (MultipartFile uploadFile : uploadFiles) {
            File file = new File(uploadPath, uploadFile.getOriginalFilename());
            file.mkdirs();
            uploadFile.transferTo(file);
        }
        return ResponseWrapper.success();
    }

    /**
     * 获取子文件
     *
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
     *
     * @param path
     * @throws Exception
     */
    public void removeFile(String path) throws Exception {
        File file = new File(getRootPath() + "/" + path);
        removeCurrentAndChild(file);
    }

    /**
     * 获取根目录
     *
     * @return
     */
    private String getRootPath() {
        try {
            return sourceImageRootFolder.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 删除子文件
     *
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

    private List<String> getImageCanonicalFilePath(String folderPath, List<String> imageList) {
        return imageList.stream().map(imageName -> getRootPath() + "/" + folderPath + "/" + imageName).toList();
    }

    /**
     * 创建分析任务
     * @param taskRequestParam
     * @throws IOException
     */
    public ResponseWrapper createAnalysisTask(TaskRequestParam taskRequestParam) throws IOException {
        String taskName = taskRequestParam.getTaskName();
        File analysisFile = new File(getRootPath() + taskRequestParam.getPath());
        List<File> analysisFiles;
        if (analysisFile.isDirectory()) {
            analysisFiles = Arrays.stream(analysisFile.listFiles(childFile -> {
                if (childFile.isHidden() || childFile.isDirectory()) {
                    return false;
                }
                return true;
            })).toList();
        } else {
            analysisFiles = new ArrayList<>(1);
            analysisFiles.add(analysisFile);
        }
        if (analysisFiles.isEmpty()) {
            return ResponseWrapper.fail("没有可分析的图片，请选择！");
        }
        taskName = taskName + "-" + getTaskId();
        analysisTaskService.createAnalysisTask(taskName, analysisFiles, taskRequestParam.getParam());

        return ResponseWrapper.success(taskName);
    }

    private long getTaskId() {
        return System.currentTimeMillis();
    }

}
