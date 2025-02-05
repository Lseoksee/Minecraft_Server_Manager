package seok.dto;

public class SearchJavaDTO {
    String type;
    String version;
    String path;
    String arch;

    public SearchJavaDTO(String type, String version, String path, String arch) {
        this.type = type;
        this.version = version;
        this.path = path;
        this.arch = arch;
    }

    public SearchJavaDTO() {
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArch() {
        return arch;
    }

    public void setArch(String arch) {
        this.arch = arch;
    }
}
