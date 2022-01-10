package image.analysis.cloud.app.application.service;

import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.entrypoint.web.WebConfig;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class FileSystemService {

    private String fileRootPath;
    private String resourceRootPath;

    /**
     * @param fileRootPath 文件系统根路径
     * @param resourceRootPath 资源访问根路径
     */
    public FileSystemService(String fileRootPath, String resourceRootPath) {
        this.fileRootPath = fileRootPath;
        this.resourceRootPath = resourceRootPath;
    }

    /**
     * 获取文件可以通过http服务访问的路径
     * @param file
     * @return
     * @throws IOException
     */
    private String getResourcePathByFile(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        return resourceRootPath + canonicalPath.replace(fileRootPath, "");
    }

    /**
     * 获取文件相对路径
     * @param file
     * @return
     */
    private String getPathByFile(File file) throws IOException {
        String canonicalPath = file.getCanonicalPath();
        return canonicalPath.replace(fileRootPath, "");
    }

    /**
     * 获取文件系统绝对路径
     * @param path
     * @return
     */
    private String getCanonicalPath(String path) {
        if (StringUtils.isEmpty(path)) {
            path = "";
        }
        return fileRootPath + path;
    }

    public List<FileSystem> listByParentPath(String parentPath, String name) throws IOException {
        String parentCanonicalPath = getCanonicalPath(parentPath);

        File parentDir = new File(parentCanonicalPath);
        File[] files = parentDir.listFiles(pathname -> !pathname.isHidden());
        List<FileSystem> res = new LinkedList<>();
        String canonicalParentPath = parentDir.getCanonicalPath();
        if (files != null) {
            for (File file: files) {
                FileSystem fileSystem = new FileSystem();
                fileSystem.setName(file.getName());
                fileSystem.setPath(getPathByFile(file));
                fileSystem.setParentPath(parentPath);
                fileSystem.setCanonicalFilePath(file.getCanonicalPath());
                fileSystem.setCanonicalParentFilePath(canonicalParentPath);
                fileSystem.setResourcePath(getResourcePathByFile(file));
                fileSystem.setDir(file.isDirectory());
                res.add(fileSystem);
            }
        }
        return res;
    }



    public ResponseWrapper addFile(MultipartFile[] uploadFiles, String parentPath) throws IOException {
        String parentCanonicalPath = getCanonicalPath(parentPath);
        for (MultipartFile uploadFile:uploadFiles) {
            String path = parentCanonicalPath + "/" + uploadFile.getOriginalFilename();
            File file = new File(path, uploadFile.getOriginalFilename());
            file.mkdirs();
            uploadFile.transferTo(file);
        }
        return ResponseWrapper.success();
    }

    /**
     * 新增文件夹
     * @param name
     * @param parentPath
     */
    public void addFolder(String name, String parentPath) {
        String parentCanonicalPath = getCanonicalPath(parentPath);
        String folderPath = parentCanonicalPath + "/" + name;
        File file = new File(folderPath);
        file.mkdirs();
    }

    public void removeFile(String path) {
        String canonicalPath = getCanonicalPath(path);
        File file = new File(canonicalPath);
        file.delete();
    }
}
