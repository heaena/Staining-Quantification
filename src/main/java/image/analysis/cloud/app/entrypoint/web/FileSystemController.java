package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.service.FileSystemService;
import image.analysis.cloud.app.infra.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(WebConfig.restApiContextPath + "/fileSystem")
@Slf4j
public class FileSystemController extends BaseController{

    @Autowired
    private FileSystemService fileSystemService;

    /**
     * 文件夹查询
     * @param name 模糊查询
     * @return
     * @throws IOException
     */
    @GetMapping("/listFolder")
    public ResponseWrapper listFolder(String name) throws IOException {
        List<FileSystem> imageList = fileSystemService.listFolder(name);
        return ResponseWrapper.success(imageList);
    }

    /**
     * 图片查询
     * @param name 模糊查询
     * @return
     * @throws IOException
     */
    @GetMapping("/listImage")
    public ResponseWrapper listImage(String folderName, String name) throws IOException {
        List<FileSystem> imageList = fileSystemService.listImage(folderName, name);
        return ResponseWrapper.success(imageList);
    }

    @PostMapping("/addFolder")
    public ResponseWrapper add(String name) {
        fileSystemService.addFolder(name);
        return ResponseWrapper.success();
    }

    @PostMapping("/uploadFile")
    public ResponseWrapper upload(@RequestParam("files") MultipartFile[] uploadFiles, String folderName) throws IOException {
        return fileSystemService.addFile(uploadFiles, folderName);
    }

    @DeleteMapping("/removeFolder")
    public ResponseWrapper removeFolder(@RequestParam("folderName") String folderName) {
        fileSystemService.removeFolder(folderName);
        return ResponseWrapper.success();
    }

    @DeleteMapping("/removeImage")
    public ResponseWrapper removeImage(@RequestParam("folderName") String folderName, @RequestParam("imageName") String imageName) {
        fileSystemService.removeImage(folderName, imageName);
        return ResponseWrapper.success();
    }
}
