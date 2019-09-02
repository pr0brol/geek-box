package client;

import java.io.File;

public class Data {


    private final static String PATH = "client/client_storage/";
    private static File file = new File(PATH);
    private static String[] filesNames = file.list();

    public static String[] getFilesNames() {


        return filesNames;
    }

    public static String getPATH() {
        return PATH;
    }

    public static File getFile() {
        return file;
    }
}
