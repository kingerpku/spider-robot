package com.spider;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by wsy on 2015/11/4.
 *
 * @author wsy
 */
public class CopyJar {

    public static void main(String[] args) throws IOException {

        String source1 = "E:\\idea-workspace\\robot\\target\\spider-robot.jar";
        String dest1 = "k:\\tmp\\spider-robot.jar";
        FileUtils.copyFile(new File(source1), new File(dest1));
        System.out.println("copy spider-robot.jar success");

        String source2 = "E:\\idea-workspace\\matlab-service\\target\\matlab-service.jar";
        String dest2 = "k:\\tmp\\matlab-service.jar";
        FileUtils.copyFile(new File(source2), new File(dest2));
        System.out.println("copy matlab-service.jar success");
    }
}
