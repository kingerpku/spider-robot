package com.spider.utils;

import java.io.File;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 
 * @ClassName: MailUtils
 * @author shifangfang
 * @date 2014年6月5日 下午1:19:49
 * 
 */
public class MailUtils {

    private static JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();

    private static MimeMessage mailMessage = null;

    private static MimeMessageHelper messageHelper;

    public static void init() {

        // 设定mail server
        senderImpl.setHost("smtp.163.com");
        try {
            // 建立邮件消息,发送简单邮件和html邮件的区别
            // 注意这里的boolean,等于真的时候才能嵌套图片，在构建MimeMessageHelper时候，所给定的值是true表示启用，
            // multipart模式 为true时发送附件 可以设置html格式
            mailMessage = senderImpl.createMimeMessage();
            messageHelper = new MimeMessageHelper(mailMessage, true, "utf-8");
            // 发件箱
            messageHelper.setFrom("mobospider@163.com");
            senderImpl.setUsername("mobospider@163.com");
            senderImpl.setPassword("mobo1234");
            Properties prop = new Properties();
            prop.put("mail.smtp.auth", "true"); // 将这个参数设为true，让服务器进行认证,认证用户名和密码是否正确
            prop.put("mail.smtp.timeout", "25000");
            senderImpl.setJavaMailProperties(prop);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 
     * @Title: send
     * @param @param subject
     * @param @param content
     * @param @param sendAddress
     * @param @param ccAddress 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void send(String subject, String content, String sendAddress, String ccAddress) {

        send(subject, content, sendAddress, ccAddress, null);
    }

    /**
     * 
     * @Title: send
     * @param @param subject
     * @param @param content
     * @param @param sendAddress
     * @param @param ccAddress 多个用分号分割
     * @param @param files 设定附件 多个用分号分割
     * @return void 返回类型
     * @throws
     */
    public static void send(String subject, String content, String sendAddress, String ccAddress, String files) {

        try {
            init();
            messageHelper.setSubject(subject);
            // true 表示启动HTML格式的邮件
            messageHelper.setText(content, true);
            if (null != files) {
                String[] fs = files.split(";");
                String filename = "";
                for (String filepath : fs) {
                    if (filepath.trim().length() > 0) {
                        FileSystemResource file = new FileSystemResource(new File(filepath));
                        // 这里的方法调用和插入图片是不同的。
                        if (filepath.contains("/")) {
                            filename = filepath.substring(filepath.lastIndexOf("/") + 1);
                        } else {
                            filename = filepath.substring(filepath.lastIndexOf("\\") + 1);
                        }
                        messageHelper.addAttachment(filename, file);
                    }

                }

            }

            // 设置收件人，寄件人
            messageHelper.setTo(InternetAddress.parse(sendAddress));
            if (ccAddress.length() > 0) {
                messageHelper.setCc(ccAddress.split(";"));
            }

            // 发送邮件
            senderImpl.send(mailMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     * @Title: send
     * @param @param subject
     * @param @param content
     * @param @param sendAddress
     * @param @param ccAddress 抄送人
     * @param @param files 附件，文件绝对地址 无附件就为null
     * @return void 返回类型
     * @throws
     */
    public static void send(String subject, String content, String sendAddress, String[] ccAddress, String[] files) {

        if (null == ccAddress || ccAddress.length == 0) {
            ccAddress = new String[] { sendAddress };
        }
        if (null == files || files.length == 0) {
            send(subject, content, sendAddress, ccAddress);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : ccAddress) {
                sb.append(s).append(";");
            }
            StringBuilder filesstr = new StringBuilder();
            for (String file : files) {
                filesstr.append(file).append(";");
            }
            send(subject, content, sendAddress, sb.substring(0, sb.length() - 1), filesstr.substring(0, filesstr.length() - 1));
        }

    }

    /**
     * 
     * @Title: send
     * @param @param subject
     * @param @param content
     * @param @param sendAddress
     * @param @param ccAddress 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void send(String subject, String content, String sendAddress, String[] ccAddress) {

        StringBuilder sb = new StringBuilder();
        for (String s : ccAddress) {
            sb.append(s).append(";");
        }
        send(subject, content, sendAddress, sb.substring(0, sb.length() - 1));
    }

}
