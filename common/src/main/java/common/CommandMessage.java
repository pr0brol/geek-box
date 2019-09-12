package common;

public class CommandMessage extends AbstractMessage {
    public static String COPY = "/copy";
    public static String DELETE = "/delete";
    public static String REFRESH = "/refresh";

    private final String text;
    private final String path;

    public CommandMessage(String text, String path){
        this.text = text;
        this.path = path;
    }

    public String getText() {
        return text;
    }

    public String getPath() {
        return path;
    }
}
