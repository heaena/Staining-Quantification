package image.analysis.cloud.app.entrypoint.web;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.application.domain.model.AnalysisTask;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisResult;
import image.analysis.cloud.app.application.service.AnalysisService;
import image.analysis.cloud.app.application.service.FileSystemService;
import image.analysis.cloud.app.application.service.SourceImageService;
import image.analysis.cloud.app.infra.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(WebConfig.restApiContextPath + "/sourceImage")
@Slf4j
public class SourceImageController extends BaseController{
    @Resource
    private SourceImageService sourceImageService;
    @Resource
    private AnalysisService analysisService;

    @PostMapping("/createTask")
    public ResponseWrapper createTask(@RequestBody TaskRequestParam taskRequestParam) {
        List<String> imageListObj = new ArrayList<>();
        if (StringUtils.isNotEmpty(taskRequestParam.getImageList())) {
            imageListObj = JSONObject.parseArray(taskRequestParam.getImageList()).toJavaList(String.class);
        }
        sourceImageService.createTask(taskRequestParam.getTaskName(), taskRequestParam.getFolderName(), imageListObj, taskRequestParam.getParam());
        return ResponseWrapper.success();
    }

}
