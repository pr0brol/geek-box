package client;

import java.io.File;

public class Data {

    private final String PATH = "client/client_storage/";

    public String[] getFilesNames() {
        File file = new File(PATH);
        String[] filesNames = file.list();
        return filesNames;
    }

    public String getPATH() {
        return PATH;
    }

}
