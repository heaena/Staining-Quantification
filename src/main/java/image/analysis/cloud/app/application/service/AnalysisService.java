package image.analysis.cloud.app.application.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.AnalysisTask;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.repo.AnalysisTaskRepo;
import image.analysis.cloud.app.application.domain.repo.FileSystemRepo;
import image.analysis.cloud.app.infra.ResponseWrapper;
import image.analysis.cloud.app.infra.rpc.RemoteAnalysisPlatformService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class AnalysisService {
    @Resource
    private AnalysisTaskRepo analysisTaskRepo;
    @Resource
    private FileSystemRepo fileSystemRepo;
    @Resource
    private RemoteAnalysisPlatformService remoteAnalysisPlatformService;

    private ExecutorService executorService = Executors.newFixedThreadPool(100);

    public List<AnalysisTask> listParams(String name) {
        QueryWrapper queryWrapper = new QueryWrapper();
        if (StringUtils.isNotEmpty(name)) {
            queryWrapper.like("name", "%" + name + "%");
        }
        return analysisTaskRepo.list(queryWrapper);
    }

    public void saveOrUpdateParam(AnalysisTask analysisTask) {
        analysisTaskRepo.saveOrUpdate(analysisTask);
    }

    public void removeParam(String id) {
        analysisTaskRepo.removeById(id);
    }

    public ResponseWrapper startTask(String taskName, String param, String path) {
        File file = new File(path);
        if (file.isFile()) {

        }
        String inputPath = null;
        String outputPath = null;
        String taskId = inputPath;
        executorService.submit(() -> {
            ResponseWrapper response = remoteAnalysisPlatformService.startTask(taskId, inputPath, outputPath, JSONObject.parseObject(param));
            if (response.isSuccess()) {

            } else {

            }
        });
        return ResponseWrapper.success("任务已提交，请稍后查看结果");
    }

    public List<AnalysisTask> getAnalysisTask(String fileId) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("file_id", fileId);
        List<AnalysisTask> analysisTaskList = analysisTaskRepo.list(queryWrapper);
        analysisTaskList.stream().forEach(item->{
            String outputFileSystemPath = item.getOutputFileSystemPath();
            File file = new File(outputFileSystemPath);
            if (file.exists()) {
                String[] list = file.list();
                if (list.length > 0) {
                    List<FileSystem> fileSystems = new LinkedList<>();
                    for (String fileName:list) {
                        FileSystem fileSystem = new FileSystem();
                        fileSystem.setName(fileName);
//                        fileSystem.setPath(item.getOutputPath() + "/" + fileName);
                        fileSystems.add(fileSystem);
                    }
                    item.setImageList(fileSystems);
                }
            }
        });
        return analysisTaskList;
    }
}
