package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.domain.model.AnalysisTask;
import image.analysis.cloud.app.application.service.AnalysisService;
import image.analysis.cloud.app.application.service.FileSystemService;
import image.analysis.cloud.app.infra.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/analysis")
@Slf4j
public class AnalysisController extends BaseController{

    private FileSystemService fileSystemService;
    @Resource
    private AnalysisService analysisService;
    @GetMapping("/param/list")
    public ResponseWrapper listParams(String name) {
        //如果parentId为null，则查询当前用户的文件夹
        List<AnalysisTask> list = analysisService.listParams(name);
        return ResponseWrapper.success(list);
    }

    /*@PostMapping("/param/save")
    public ResponseWrapper saveOrUpdate(String id, @RequestParam("name") String name, @RequestParam("param") String param) {
        AnalysisTask analysisTask = null;
        if (StringUtils.isEmpty(id)) {
            analysisTask = new AnalysisTask(currentUser(), name, param);
        } else {
            analysisTask = new AnalysisTask();
            analysisTask.setName(name);
            analysisTask.setParam(param);
            analysisTask.setId(id);
        }
        analysisService.saveOrUpdateParam(analysisTask);
        return ResponseWrapper.success();
    }*/

    @DeleteMapping("/param/remove")
    public ResponseWrapper removeFile(@RequestParam("id") String id) throws IOException {
        analysisService.removeParam(id);
        return ResponseWrapper.success();
    }

    @PostMapping("/startTask")
    public ResponseWrapper startTask(@RequestParam("taskName")String taskName, @RequestParam("param") String param, @RequestParam("path") String path) {
        analysisService.startTask(taskName, param, path);
        return ResponseWrapper.success();
    }

    @GetMapping("/listAnalysisResult")
    public ResponseWrapper listAnalysisResult(@RequestParam("fileId") String fileId) {
        List<AnalysisTask> list = analysisService.getAnalysisTask(fileId);
        return ResponseWrapper.success(list);
    }
}
