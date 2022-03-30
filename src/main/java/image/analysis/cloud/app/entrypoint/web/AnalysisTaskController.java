package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.domain.model.AnalysisTaskResult;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisResult;
import image.analysis.cloud.app.application.service.AnalysisTaskService;
import image.analysis.cloud.app.infra.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(WebConfig.restApiContextPath + "/analysisTask")
@Slf4j
public class AnalysisTaskController {

    @Autowired
    private AnalysisTaskService analysisTaskService;

    /**
     * 查询任务
     * @param filterName 模糊查询
     * @return
     * @throws IOException
     */
    @GetMapping("/listTask")
    public ResponseWrapper listTask(String filterName) throws IOException {
        //如果parentId为null，则查询当前用户的文件夹
        List<FileSystem> imageList = analysisTaskService.listTask(filterName);
        return ResponseWrapper.success(imageList);
    }

    @GetMapping("/taskResult")
    public ResponseWrapper taskResult(String taskName, String filterName) throws IOException {
        AnalysisTaskResult imageAnalysisResult  = analysisTaskService.taskResult(taskName, filterName);
        return ResponseWrapper.success(imageAnalysisResult);
    }

}
