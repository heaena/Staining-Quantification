package image.analysis.cloud.app.application.domain.model;


public class FileSystem {

    private String name;
    private String path;
    private String parentPath;
    private String canonicalFilePath;
    private String canonicalParentFilePath;
    private String resourcePath;
    private boolean isDir;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCanonicalFilePath() {
        return canonicalFilePath;
    }

    public void setCanonicalFilePath(String canonicalFilePath) {
        this.canonicalFilePath = canonicalFilePath;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
    }

    public boolean isDir() {
        return isDir;
    }

    public void setDir(boolean dir) {
        isDir = dir;
    }

    public String getParentPath() {
        return parentPath;
    }

    public void setParentPath(String parentPath) {
        this.parentPath = parentPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCanonicalParentFilePath() {
        return canonicalParentFilePath;
    }

    public void setCanonicalParentFilePath(String canonicalParentFilePath) {
        this.canonicalParentFilePath = canonicalParentFilePath;
    }
}
