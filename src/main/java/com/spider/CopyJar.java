package com.spider;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wsy on 2015/11/4.
 */
public class CopyJar {

    public static void main(String[] args) throws IOException {

        FileUtils.copyFile(new File("E:\\idea-workspace\\robot\\target\\spider-robot.jar"), new File("k:\\tmp\\spider-robot.jar"));
        System.out.println("copy spider-robot.jar success");
        FileUtils.copyFile(new File("E:\\idea-workspace\\matlab-service\\target\\matlab-service.jar"), new File("k:\\tmp\\matlab-service.jar"));
        System.out.println("copy matlab-service.jar success");
    }
}
