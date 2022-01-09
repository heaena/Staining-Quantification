package image.analysis.cloud.app.application.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.repo.FileSystemRepo;
import image.analysis.cloud.app.infra.ResponseWrapper;
import image.analysis.cloud.app.infra.util.IdUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileSystemService {
    @Resource
    private FileSystemRepo fileSystemRepo;
    public List<FileSystem> listByParentId(String parentId, String name) {
        if (StringUtils.isEmpty(parentId)) {
            FileSystem userRootFolder = getAndCheckUserRootFolder();
            parentId = userRootFolder.getId();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("parent_id", parentId);
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", "%" + name + "%");
        }
        return fileSystemRepo.list(queryWrapper);
    }

    public ResponseWrapper addFile(MultipartFile[] uploadFiles, String folderId) throws IOException {
        FileSystem parentFileSystem = null;
        if (StringUtils.isEmpty(folderId)) {
            parentFileSystem = getAndCheckUserRootFolder();
        } else {
            parentFileSystem = fileSystemRepo.getById(folderId);
        }

        parentFileSystem.checkExists();
        String parentPath = parentFileSystem.getFilePath();
        Date date = new Date();
        List<FileSystem> imageList = new LinkedList<>();
        for (MultipartFile uploadFile:uploadFiles) {
            FileSystem image = new FileSystem(uploadFile.getOriginalFilename(), date, parentFileSystem, FileSystem.TYPE_FILE);
            imageList.add(image);
            uploadFile.transferTo(new File(parentPath, uploadFile.getOriginalFilename()));
        }
        fileSystemRepo.saveBatch(imageList);
        return ResponseWrapper.success();
    }

    public void addFolder(String name, String parentId) {
        FileSystem parentFileSystem = null;
        if (StringUtils.isEmpty(parentId)) {
            //查询用户的根目录
            parentFileSystem = getAndCheckUserRootFolder();
        } else {
            parentFileSystem = fileSystemRepo.getById(parentId);
        }

        FileSystem fileSystem = new FileSystem(name, new Date(), parentFileSystem, FileSystem.TYPE_DIR);
        fileSystemRepo.save(fileSystem);
    }

    public FileSystem getAndCheckUserRootFolder() {
        FileSystem userRootFileSystem = null;
        userRootFileSystem = getUserRootFolder();
        if (userRootFileSystem == null) {
            userRootFileSystem = addUserRootFolder();
        }
        return userRootFileSystem;
    }

    private FileSystem addUserRootFolder() {
        FileSystem fileSystem = new FileSystem();
        fileSystem.setId(IdUtil.uuid());
        fileSystem.setPath(AnalysisConfig.getImgAnalysisInputPath() + "/");
        fileSystem.setCreateDate(new Date());
        fileSystem.checkExists();
        fileSystemRepo.save(fileSystem);
        return fileSystem;
    }

    public FileSystem getUserRootFolder() {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.isNull("parent_id");
        return fileSystemRepo.getOne(queryWrapper);
    }

    public void removeFile(String id) {
        FileSystem fileSystem = fileSystemRepo.getById(id);
        fileSystemRepo.removeById(id);
        File file = fileSystem.getLocalFile();
        file.delete();
    }
}
