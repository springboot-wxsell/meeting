package com.stylefeng.guns.rest.common.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.*;

/**
 * @author : wangwei
 * @date : Created in 2020/2/23 16:53
 * @description: ${description}
 * @modified By:
 * @version: ${version}
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FTPUtils {

    // ip, port, user, password

    private String hostname;

    private int port;

    private String username;

    private String password;

    private String uploadPath;

    private FTPClient ftpClient = null;

    private void initFTPClient() {
        try {
            ftpClient = new FTPClient();
            ftpClient.setControlEncoding("utf-8");
            ftpClient.connect(hostname, port);
            ftpClient.login(username, password);

        } catch (Exception e) {
            log.error("初始化ftp失败", e);
        }
    }

    /**
     * 获取文件内容以字符输出
     *
     * @param fileAddress
     * @return
     */
    public String getFileStrByAddress(String fileAddress) {
        BufferedReader reader = null;
        try {
            initFTPClient();
            reader = new BufferedReader(new InputStreamReader(
                    ftpClient.retrieveFileStream(fileAddress)));
            StringBuffer buffer = new StringBuffer();
            while (true) {
                String lineStr = reader.readLine();
                if (lineStr == null) {
                    break;
                }
                buffer.append(lineStr);
            }
            ftpClient.logout();
            return buffer.toString();
        } catch (Exception e) {
            log.error("获取文件信息失败");
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }


    /**
     * 上传文件
     *
     * @param fileName
     * @param file
     * @return
     */
    public boolean uploadFile(String fileName, File file) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            // 初始化
            initFTPClient();
            // 设置 ftp 的相关参数
            ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
            ftpClient.enterLocalPassiveMode();

            // 将 ftpClient 的工作空间修改
            ftpClient.changeWorkingDirectory(this.uploadPath);

            // 上传文件
            ftpClient.storeFile(fileName, fis);
            return true;
        } catch (Exception e) {
            log.error("上传失败", e);
        } finally {
            try {
                fis.close();
                ftpClient.logout();
            } catch (IOException e) {
                log.error("关闭文件输入流失败", e);
            }
        }
        return false;
    }

}
