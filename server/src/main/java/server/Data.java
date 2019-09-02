package server;

import java.io.File;

public class Data {


    private final static String PATH = "server/server_storage/";
    static File file = new File(PATH);
    static File[] filesNames = file.listFiles();


    public static File[] getFilesNames() {
        return filesNames;
    }
}
