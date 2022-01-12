package image.analysis.cloud.app.application.domain.model;

import java.util.ArrayList;
import java.util.List;

public class ImageAnalysisResult {
    private String folderName;
    private String imageName;
    private String imageCanonicalPath;
    private String imageResourcePath;
    private String outputPath;
    private List<OutputItem> outputItemList = new ArrayList<>();

    public String getImageCanonicalPath() {
        return imageCanonicalPath;
    }

    public void setImageCanonicalPath(String imageCanonicalPath) {
        this.imageCanonicalPath = imageCanonicalPath;
    }

    public String getFolderName() {
        return folderName;
    }

    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public List<OutputItem> getOutputItemList() {
        return outputItemList;
    }

    public void addOutputItem(OutputItem outputItem) {
        this.outputItemList.add(outputItem);
    }

    public String getImageResourcePath() {
        return imageResourcePath;
    }

    public void setImageResourcePath(String imageResourcePath) {
        this.imageResourcePath = imageResourcePath;
    }

    public static class OutputItem {
        private String taskName;
        private List<OutputFile> outputFiles = new ArrayList<>();

        public String getTaskName() {
            return taskName;
        }

        public void setTaskName(String taskName) {
            this.taskName = taskName;
        }

        public List<OutputFile> getOutputFiles() {
            return outputFiles;
        }

        public void addOutputFile(OutputFile outputFile) {
            this.outputFiles.add(outputFile);
        }
    }

    public static class OutputFile {
        private int type;
        private String name;
        private String canonicalPath;
        private String resourcePath;
        private String data;

        public String getCanonicalPath() {
            return canonicalPath;
        }

        public void setCanonicalPath(String canonicalPath) {
            this.canonicalPath = canonicalPath;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResourcePath() {
            return resourcePath;
        }

        public void setResourcePath(String resourcePath) {
            this.resourcePath = resourcePath;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }

}
