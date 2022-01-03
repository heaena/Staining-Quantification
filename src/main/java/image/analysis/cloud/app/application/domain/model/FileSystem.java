package image.analysis.cloud.app.application.domain.model;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import image.analysis.cloud.app.entrypoint.config.InitConfig;
import image.analysis.cloud.app.infra.util.IdUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;

@TableName("file_system")
public class FileSystem {
    public static final String UPLOAD_PATH = "/input";
    //文件夹
    public static final int TYPE_DIR = 1;
    //文件
    public static final int TYPE_FILE = 2;

    private String id;
    private String name;
    private String path;
    private String parentId;
    private String userId;
    private Date createDate;
    private int type;

    @TableField(exist = false)
    private String fileUri;

    public FileSystem() {
    }

    public FileSystem(String name, Date createDate, FileSystem parentFileSystem, int type) {
        this.id = IdUtil.uuid();
        this.name = name;
        this.createDate = createDate;
        this.parentId = parentFileSystem.getId();
        this.userId = parentFileSystem.getUserId();
        this.path = parentFileSystem.getPath() + "/" + name;
        this.type = type;
    }

    public void checkExists() {
        File localFile = getLocalFile();
        if (!localFile.exists()) {
            localFile.mkdirs();
        }
    }

    public String getFilePath() {
        if (StringUtils.isEmpty(parentId)) {
            //用户根目录
            return getUploadRootPath() + "/" + name;
        } else {
            return InitConfig.getRootFilePath() + "/" + path;
        }
    }

    public File getLocalFile() {
        return new File(getFilePath());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public static String getUploadRootPath() {
        return InitConfig.getRootFilePath() + UPLOAD_PATH;
    }
}
