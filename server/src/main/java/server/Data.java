package server;

import java.io.File;

public class Data {

    private final static String PATH = "server/server_storage/";

    public String[] getFilesNames(String login) {
        String path = PATH + login + "/";
        File file = new File(path);
        String[] filesNames = file.list();
        return filesNames;
    }

    public String getPATH() {
        return PATH;
    }
}
