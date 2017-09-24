/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.alphonse92.diskfiller.util;

/**
 *
 * @author Eliecer Alejandro Molina Vergel <alejandro_mover@hotmail.com>
 */
public class DebugUtil {

    public static byte TYPE_ERROR = 0;
    public static byte TYPE_NORMAL = 1;

    public static void debug(byte type, String name, String method, String message) {
        if (type == DebugUtil.TYPE_ERROR) {
            System.err.println(name + "." + method + " -> " + message);
        } else if (type == DebugUtil.TYPE_NORMAL) {
            System.out.println(name + "." + method + " -> " + message);
        }

    }
}
