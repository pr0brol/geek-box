package common;

public class MyMessage extends AbstractMessage {

    private String text;

    public String getText() {
        return text;
    }

    public MyMessage(String text) {
        this.text = text;
    }


}
