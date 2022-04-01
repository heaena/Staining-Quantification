package image.analysis.cloud.app.application.service;

import au.com.bytecode.opencsv.CSVReader;
import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.application.domain.model.AnalysisTaskResult;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisResult;
import image.analysis.cloud.app.application.domain.model.ImageAnalysisTask;
import image.analysis.cloud.app.infra.ResponseWrapper;
import image.analysis.cloud.app.infra.rpc.RemoteAnalysisPlatformService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class AnalysisTaskService implements ImageService {

    private ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final String outputRoot = "/output";

    /**
     * 创建分析任务
     * @param taskName 任务名称
     * @param analysisFiles 源图片集合
     * @param param 任务参数
     * @return
     */
    public void createAnalysisTask(String taskName, List<File> analysisFiles, String param) throws IOException {
        long taskId = getTaskId();
        List<ImageAnalysisTask> tasks = new ArrayList<>();
        for (File file: analysisFiles) {
            String outputFolderPath = getOutputPath(taskId, taskName);
            ImageAnalysisTask imageAnalysisTask = new ImageAnalysisTask(taskId, taskName, file.getCanonicalPath(), outputFolderPath, param);
            tasks.add(imageAnalysisTask);
        }
        submitTask(tasks);
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

    /**
     * 查询任务
     * @param filterName 模糊查询
     * @return
     */
    public List<FileSystem> listTask(String filterName) {
        File file = new File(getRootPath());
        if (!file.exists()) {
            file.mkdir();
        }
        return Arrays.stream(file.listFiles(childFile -> {
            // 过滤文件
            if (childFile.isHidden()) {
                return false;
            }
            if (StringUtils.isNotEmpty(filterName)) {
                return childFile.getName().matches(".*" + filterName + ".*");
            }
            return true;
        })).map(item -> {
            // 封装返回结果
            FileSystem fileSystem = new FileSystem();
            try {
                fileSystem.setName(item.getName());
                fileSystem.setPath(item.getCanonicalPath().replace(getRootPath(), ""));
                fileSystem.setCanonicalFilePath(item.getCanonicalPath());
                fileSystem.setLastModified(item.lastModified());
                fileSystem.setDir(true);
            } catch (IOException e) {
                log.error("文件读取异常", e);
            }
            return fileSystem;
        }).toList();
    }


    public AnalysisTaskResult taskResult(String taskName, String filterName) throws IOException {
        AnalysisTaskResult result = new AnalysisTaskResult();

        File taskResultfolder = getTaskResultFile(taskName);

        result.setTaskName(taskName);
        result.setTaskTime(taskResultfolder.lastModified());

        //查询分析结果文件
        File []  analysisResultFiles = taskResultfolder.listFiles(file -> {
            if (file.isHidden() || file.isDirectory()) {
                return false;
            }
            if (StringUtils.isNotEmpty(filterName)) {
                return file.getName().matches(".*" + filterName + ".*");
            }
            return true;
        });

        if (analysisResultFiles.length > 0) {
            //分析结果数据
            result.setData(getOutputData(taskName));
            //分析结果图片
            for (File file : analysisResultFiles) {
                FileSystem fileSystem = new FileSystem();
                fileSystem.setName(file.getName());
                fileSystem.setResourcePath(getResourcePath(file));
                fileSystem.setDir(false);
                result.addImage(fileSystem);
            }
        }

        return result;
    }

    /**
     * 获取分析结果数据
     * @param taskName
     * @return
     */
    private String getOutputData(String taskName) throws IOException {
        File outputDataFile = new File(getRootPath() + "/" + taskName + "/out_stats/out_stats_all.csv");
        return JSONObject.toJSONString(parseCsv(outputDataFile));
    }

    private List<String []> parseCsv(File file) throws IOException {
        DataInputStream dataInputStream = new DataInputStream(new FileInputStream(file));
        return new CSVReader(new InputStreamReader(dataInputStream,"UTF-8")).readAll();
    }

    private File getTaskResultFile(String taskName) {
        return new File(getRootPath() + "/" + taskName);
    }

}
