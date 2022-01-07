package image.analysis.cloud.app.application.domain.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.infra.util.IdUtil;

import java.io.File;
import java.util.Date;
import java.util.List;

@TableName("analysis_task")
public class AnalysisTask {
    public static final String OUTPUT_PATH = "/output";
    @TableId
    private String id;
    private String fileId;
    private String name;
    private String outputPath;
    //json
    private String param;
    private Date createDate;

    @TableField(exist = false)
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

    private void checkFileExists() {
        File file = new File(getOutputFileSystemPath());
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public String getOutputFileSystemPath() {
        return AnalysisConfig.getImgAnalysisOutputPath();
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
