package image.analysis.cloud.app.application.service;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisTask;
import image.analysis.cloud.app.infra.ResponseWrapper;
import image.analysis.cloud.app.infra.rpc.RemoteAnalysisPlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AnalysisTaskService {

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final String outputRoot = "/output";

    /**
     * 创建分析任务
     * @param taskName 任务名称
     * @param imageList 源图片集合
     * @param param 任务参数
     * @return
     */
    public List<ImageAnalysisTask> createAnalysisTask(String taskName, List<String> imageList, String param) {
        long taskId = getTaskId();
        List<ImageAnalysisTask> tasks = new ArrayList<>();
        for (String imageCanonicalPath: imageList) {
            String outputFolderPath = getOutputPath(taskId, taskName);
            ImageAnalysisTask imageAnalysisTask = new ImageAnalysisTask(taskId, taskName, imageCanonicalPath, outputFolderPath, param);
            tasks.add(imageAnalysisTask);
        }
        return tasks;
    }

    /**
     * 向线程池提交分析任务
     * @param tasks
     */
    public void submitTask(List<ImageAnalysisTask> tasks) {
        tasks.stream().forEach(item -> {
            executorService.submit(() -> {
                File outputFolder = new File(item.getOutputFolderPath());
                if (!outputFolder.exists()) {
                    outputFolder.mkdirs();
                }
                ResponseWrapper response = RemoteAnalysisPlatformService.executeTask(item.getTaskId(), item.getTaskName(), item.getImagePath(), item.getOutputFolderPath(), JSONObject.parseObject(item.getParam()), null);
                if (response.isSuccess()) {

                } else {

                }
            });
        });

    }

    private String getOutputPath(long taskId, String taskName) {
        return getRootPath() + outputRoot + "/" + taskId + "-" + taskName;
    }

    /**
     * 分析结果根目录
     * @return
     */
    private String getRootPath() {
        return AnalysisConfig.getImgAnalysisWorkspacePath() + outputRoot;
    }


    private long getTaskId() {
        return System.currentTimeMillis();
    }

}
