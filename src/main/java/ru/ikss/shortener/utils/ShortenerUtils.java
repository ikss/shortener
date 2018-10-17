package ru.ikss.shortener.utils;

public class ShortenerUtils {
    private static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ";
    private static final int BASE = ALPHABET.length();

    public static String encode(long num) {
        StringBuilder str = new StringBuilder();
        if (num == 0) {
            str.append(ALPHABET.charAt(0));
        } else {
            while (num > 0) {
                str.append(ALPHABET.charAt((int) (num % BASE)));
                num = num / BASE;
            }
        }
        return str.toString();
    }

    public static long decode(String str) {
        long num = 0;
        for (int i = str.length() - 1; i >= 0; i--) {
            num = num * BASE + ALPHABET.indexOf(str.charAt(i));
        }
        return num;
    }
}