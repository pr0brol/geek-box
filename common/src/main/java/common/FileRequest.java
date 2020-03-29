package common;

public class FileRequest extends AbstractMessage {

    private String[] filesName;

    public String[] getFilesName() {
        return filesName;
    }

    public FileRequest(String[] filesName) {
        this.filesName = filesName;
    }
}
