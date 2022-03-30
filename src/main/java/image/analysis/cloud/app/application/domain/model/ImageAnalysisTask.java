package image.analysis.cloud.app.application.domain.model;

import java.util.UUID;

public class ImageAnalysisTask {
    private String id = UUID.randomUUID().toString();
    private long taskId;
    private String taskName;
    private String imagePath;
    private String outputFolderPath;
    private String param;

    public ImageAnalysisTask(long taskId, String taskName, String imagePath, String outputFolderPath, String param) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.imagePath = imagePath;
        this.outputFolderPath = outputFolderPath;
        this.param = param;
    }

    public String getParam() {
        return param;
    }

    public String getId() {
        return id;
    }

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getOutputFolderPath() {
        return outputFolderPath;
    }

    public void setOutputFolderPath(String outputFolderPath) {
        this.outputFolderPath = outputFolderPath;
    }
}
