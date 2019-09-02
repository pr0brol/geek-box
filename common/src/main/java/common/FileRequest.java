package common;

public class FileRequest extends AbstractMessage {
    private String[] filesname;

    public String[] getFilesname() {
        return filesname;
    }

    public FileRequest(String[] filesname) {
        this.filesname = filesname;
    }
}
