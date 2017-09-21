/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller;

import com.alphonse92.diskfiller.Exception.DiskFillerException;
import com.alphonse92.diskfiller.util.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

    public DiskFiller debug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public DiskFiller createDirectories(String pathToFilePaths) throws DiskFillerException {
        this.valideStatus();

        try {
            this.filesArray = this.getFilesFromArrayPaths(this.getArrayOfPaths(pathToFilePaths));
            boolean success = true;
            for (File file : this.filesArray) {
                success = success && file.mkdirs();
            }

            if (!success) {
                this.removeRootChildren();
                this.manageException("No se pudieron crear todos los directorios",
                        new DiskFillerException("No se pudieron crear todos los directorios"));
            }

        } catch (Exception ex) {
            this.manageException(ex.getMessage(), ex);
        }

        return this;
    }

    private void manageException(String msg, Exception e) {
        this.status = false;
        this.status_message = msg;
        this.status_ex = e;
    }

    private void valideStatus() throws DiskFillerException {
        if (!this.status) {
            this.status_ex.printStackTrace();
            throw new DiskFillerException(this.status_message);
        }
    }

    private void removeRootChildren() {
        FileUtil.purgeDirectory(new File(this.root));
    }

    private ArrayList<File> getFilesFromArrayPaths(ArrayList<String> arrayPaths) {
        ArrayList<File> out = new ArrayList();
        for (String path : arrayPaths) {
            out.add(new File(this.root + File.separator + path));
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

    //getters and setters
    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

}
