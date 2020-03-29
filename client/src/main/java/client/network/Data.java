package client.network;

import java.io.File;

public class Data {

    private final String PATH = "client/client_storage/";

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
