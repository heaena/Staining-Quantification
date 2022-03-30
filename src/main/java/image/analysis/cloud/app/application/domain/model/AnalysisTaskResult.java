package image.analysis.cloud.app.application.domain.model;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class AnalysisTaskResult {
    String taskName;
    long taskTime;
    // 系统路径
    String canonicalPath;
    // 分析结果数据
    String data;
    // 分析结果图片
    LinkedHashMap<String, List<FileSystem>> imageMap = new LinkedHashMap();

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public long getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(long taskTime) {
        this.taskTime = taskTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public LinkedHashMap<String, List<FileSystem>> getImageMap() {
        return imageMap;
    }

    public String getCanonicalPath() {
        return canonicalPath;
    }

    public void setCanonicalPath(String canonicalPath) {
        this.canonicalPath = canonicalPath;
    }

    /**
     * 添加图片,并按照文件名称分组
     * @param image
     */
    public void addImage(FileSystem image) {
        String key = image.getName();
        int stainedIndex = image.getName().lastIndexOf("-stained");
        if (stainedIndex > 0) {
            key = image.getName().substring(0, stainedIndex) + image.getName().substring(stainedIndex + 8);
        } else {
            int totalIndex = image.getName().lastIndexOf("-total");
            if (totalIndex > 0) {
                key = image.getName().substring(0, totalIndex) + image.getName().substring(totalIndex + 6);
            }
        }

        List<FileSystem> fileSystems = this.imageMap.get(key);
        if (fileSystems == null) {
            fileSystems = new ArrayList<>();
            this.imageMap.put(key, fileSystems);
        }
        fileSystems.add(image);
    }

    public static void main(String[] args) {
        String s = "s-stained.jpg";
        int i = s.lastIndexOf("-stained");
        System.out.println(s.substring(0, i) + s.substring(i + 8));
    }
}
