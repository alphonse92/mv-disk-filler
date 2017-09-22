/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller;

import com.alphonse92.diskfiller.Exception.DiskFillerException;
import com.alphonse92.diskfiller.util.DebugUtil;
import com.alphonse92.diskfiller.util.FileUtil;
import com.alphonse92.diskfiller.util.Numbers;
import com.alphonse92.diskfiller.util.StringUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class DiskFiller {

    public static byte VERBOSE_NONE = 0;
    public static byte VERBOSE_ALL = 1;
    private String root = null;
    private boolean status = true;
    private String status_message = "";
    private Exception status_ex = null;
    private ArrayList<File> filesArray = null;
    private boolean debug = false;
    private byte verbose = 0;

    public DiskFiller(String root) {
        this.root = root;
        this.verbose = DiskFiller.VERBOSE_NONE;
    }

    public DiskFiller debug(boolean debug, byte verbose) {
        this.debug = debug;
        this.verbose = verbose;
        return this;
    }

    public DiskFiller createDirectories(int numberOfDirectories, int minDepth, int maxDepth) throws DiskFillerException {
        try {
            String pathToFilePaths = "rutas";
            PrintWriter writer = new PrintWriter(pathToFilePaths, "UTF-8");
            getAleatoryPaths(numberOfDirectories, minDepth, maxDepth).forEach((path) -> {
                writer.println(path);
            });
            writer.close();
            this.createDirectories(pathToFilePaths);
        } catch (IOException e) {
            this.manageException(e.getMessage(), e);
        }

        return this;
    }

    private ArrayList<String> getAleatoryPaths(int numberOfDirectories, int minDepth, int maxDepth) {
        ArrayList<String> lines = new ArrayList();
        while (numberOfDirectories-- > 0) {
            String nameFolder = StringUtil.getRandomString(10, true, true, false);
            int nSubdiretories = Numbers.getRandomInt(minDepth, maxDepth);
            while (nSubdiretories-- > 0) {
                nameFolder += File.separator + StringUtil.getRandomString(5, true, true, false);
            }
            System.out.println(nameFolder);
            lines.add(nameFolder);
        }
        return lines;
    }

    public DiskFiller createDirectories(String pathToFilePaths) throws DiskFillerException {
        String method = "createDirectories";
        this.valideStatus();

        try {
            this.filesArray = this.getFilesFromArrayPaths(this.getArrayOfPaths(pathToFilePaths));
            boolean success = true;
            for (File file : this.filesArray) {

                success = success && file.mkdirs();
                debug(method, "Creating directory \"" + file.getAbsolutePath() + "\" " + (success ? "[OK]" : "[FAIL]"));
            }

            if (!success) {
                this.manageException("No se pudieron crear todos los directorios",
                        new Exception("No se pudieron crear todos los directorios"));
            }

        } catch (Exception ex) {
            this.manageException(ex.getMessage(), ex);
        }

        return this;
    }

    private void manageException(String msg, Exception e) throws DiskFillerException {
        this.status = false;
        this.status_message = msg;
        this.status_ex = e;
        throw new DiskFillerException(e.getMessage());
    }

    private void valideStatus() throws DiskFillerException {
        if (!this.status) {
            this.status_ex.printStackTrace();
            this.manageException(this.status_message, new DiskFillerException(this.status_message));
        }
    }

    private void removeRootChildren() {
        FileUtil.purgeDirectory(new File(this.root));
    }

    private ArrayList<File> getFilesFromArrayPaths(ArrayList<String> arrayPaths) {
        ArrayList<File> out = new ArrayList();
        for (String path : arrayPaths) {
            if (path.length() > 0) {
                out.add(new File(this.root + File.separator + path));
            }
        }
        return out;
    }

    private ArrayList<String> getArrayOfPaths(String pathToFilePaths) throws FileNotFoundException, IOException {
        ArrayList<String> out = new ArrayList();
        FileReader fr = new FileReader(pathToFilePaths);
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            out.add(line);
        }
        return out;
    }

    private void debug(String method, String text) {
        if (this.debug && this.verbose == DiskFiller.VERBOSE_ALL) {
            DebugUtil.debug(DiskFiller.class.getSimpleName(), method, text);
        }

    }

    //getters and setters
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

}
