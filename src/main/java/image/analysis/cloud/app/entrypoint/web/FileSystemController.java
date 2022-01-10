package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.service.FileSystemService;
import image.analysis.cloud.app.infra.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/fileSystem")
@Slf4j
public class FileSystemController extends BaseController{

    private FileSystemService inputFileSystemService = new FileSystemService(AnalysisConfig.getImgAnalysisInputPath(), WebConfig.getServerContextPath() + AnalysisConfig.getInputRootPath());

    @GetMapping("/list")
    public ResponseWrapper list(String parentPath, String name) throws IOException {
        //如果parentId为null，则查询当前用户的文件夹
        List<FileSystem> imageList = inputFileSystemService.listByParentPath(parentPath, name);
        return ResponseWrapper.success(imageList);
    }

    @PostMapping("/addFolder")
    public ResponseWrapper add(String name, String parentPath) {
        inputFileSystemService.addFolder(name, parentPath);
        return ResponseWrapper.success();
    }

    @PostMapping("/uploadFile")
    public ResponseWrapper upload(@RequestParam("files") MultipartFile[] uploadFiles, String folderId) throws IOException {
        return inputFileSystemService.addFile(uploadFiles, folderId);
    }

    @DeleteMapping("/remove")
    public ResponseWrapper removeFile(@RequestParam("id") String id) throws IOException {
        inputFileSystemService.removeFile(id);
        return ResponseWrapper.success();
    }
}
