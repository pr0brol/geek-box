package server;

import java.io.File;

public class Data {

    private final static String PATH = "server/server_storage/";
    public String[] getFilesNames() {
        File file = new File(PATH);
        String[] filesNames = file.list();
        return filesNames;
    }

    public String getPATH() {
        return PATH;
    }
}
