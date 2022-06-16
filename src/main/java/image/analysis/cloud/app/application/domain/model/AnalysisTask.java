package image.analysis.cloud.app.application.domain.model;

import image.analysis.cloud.app.infra.util.IdUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

public class AnalysisTask {
    public static final String OUTPUT_PATH = "/output";
    private String id;
    private long taskId;
    private String fileId;
    private String name;
    private String outputPath;
    //json
    private String param;
    private Date createDate;

    private List<FileSystem> imageList;

    public AnalysisTask() {
    }

    public AnalysisTask(String name, String param, String fileId) {
        this.id = IdUtil.uuid();
        this.fileId = fileId;
        this.outputPath = OUTPUT_PATH + "/" + fileId + "/" + id;
        this.name = name;
        this.param = param;
        this.createDate = new Date();
        this.checkFileExists();
    }

    public AnalysisTask(long taskId, String taskName, String imagePath, String outputFolderPath) {
        this.id = IdUtil.uuid();
        this.fileId = fileId;
        this.outputPath = OUTPUT_PATH + "/" + fileId + "/" + id;
        this.name = name;
        this.param = param;
        this.createDate = new Date();
        this.checkFileExists();
    }

    private void checkFileExists() {
        File file = new File(getOutputFileSystemPath());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String getOutputFileSystemPath() {
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public List<FileSystem> getImageList() {
        return imageList;
    }

    public void setImageList(List<FileSystem> imageList) {
        this.imageList = imageList;
    }
}
