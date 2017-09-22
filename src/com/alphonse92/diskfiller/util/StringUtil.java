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
public class StringUtil {

    private static String lower = "abcdefghijklmnopqrstuvwxyz";
    private static String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static String numbers = "0123456789";
    private static String special = "¡!#$%&/()=¿?@-_.:,;{}[]*+|°¬^~<>";

    public static String getRandomString(int length, boolean upper, boolean numbers, boolean special) {
        String out = "";
        String in = StringUtil.lower;
        in += upper ? (StringUtil.upper) : "";
        in += numbers ? (StringUtil.numbers) : "";
        in += special ? (StringUtil.special) : "";

        while (length-- > 0) {
            out += in.charAt(Numbers.getRandomInt(0, in.length()));
        }

        return out;
    }

}
