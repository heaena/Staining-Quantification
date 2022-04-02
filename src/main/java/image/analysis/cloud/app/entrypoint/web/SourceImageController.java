package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.service.SourceImageService;
import image.analysis.cloud.app.infra.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * 源图片管理
 */
@RestController
@RequestMapping(WebConfig.restApiContextPath + "/sourceImage")
@Slf4j
public class SourceImageController extends BaseController{
    @Resource
    private SourceImageService sourceImageService;

    /**
     * 查询
     * @param parentPath
     * @param name 模糊查询
     * @return
     * @throws IOException
     */
    @GetMapping("/list")
    public ResponseWrapper list(String parentPath, String name) throws IOException {
        //如果parentId为null，则查询当前用户的文件夹
        List<FileSystem> imageList = sourceImageService.listSourceImage(parentPath, name);
        return ResponseWrapper.success(imageList);
    }

    /**
     * 新增文件夹
     * @param name
     * @return
     */
    @PostMapping("/addFolder")
    public ResponseWrapper add(String name) throws IOException {
        sourceImageService.addFolder(name);
        return ResponseWrapper.success();
    }

    /**
     * 上传图片
     * @param uploadFiles
     * @param parentPath
     * @return
     * @throws IOException
     */
    @PostMapping("/uploadFile")
    public ResponseWrapper upload(@RequestParam("files") MultipartFile[] uploadFiles, String parentPath) throws IOException {
        return sourceImageService.addFile(uploadFiles, parentPath);
    }

    @DeleteMapping("/remove")
    public ResponseWrapper removeFile(@RequestParam("path") String path) throws Exception {
        sourceImageService.removeFile(path);
        return ResponseWrapper.success();
    }

    /**
     * 创建分析任务
     * @param taskRequestParam 任务数据及参数
     * @return
     */
    @PostMapping("/createTask")
    public ResponseWrapper createTask(@RequestBody TaskRequestParam taskRequestParam) throws IOException {
        return sourceImageService.createAnalysisTask(taskRequestParam);
    }

}
