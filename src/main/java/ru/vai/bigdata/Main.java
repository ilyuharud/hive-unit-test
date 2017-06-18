package ru.vai.bigdata;

import java.io.File;

/**
 * Created by ilya on 18.06.17.
 */
public class Main {

    public static void main(String[] args) {

        Joiner JOINER = new Joiner(File.separator);

        JOINER.setDefaultNullValue("Не определено");

        System.out.println(JOINER.join("src", null, "tests", "java"));

    }

}
