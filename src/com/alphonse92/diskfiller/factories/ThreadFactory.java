/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller.factories;

import com.alphonse92.diskfiller.factories.threads.DiskFillerWorker;
import java.util.ArrayList;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class ThreadFactory {

    public static final byte B = 1;
    public static final byte KB = 2;
    public static final byte MB = 3;
    public static final byte GB = 4;
    public static final byte TB = 5;

    public static DiskFillerWorker getWorker(String name, ArrayList<String> workLoad, double maxBytesToWrite, byte measure) {
        double exponent = Math.pow(1024, measure);
        return new DiskFillerWorker(name, workLoad, maxBytesToWrite * exponent);
    }
}
