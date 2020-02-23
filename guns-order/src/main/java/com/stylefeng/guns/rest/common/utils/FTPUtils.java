package com.stylefeng.guns.rest.common.utils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
        }catch (Exception e) {
            log.error("获取文件信息失败");
        }finally {
            try {
                reader.close();
            }catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return null;
    }

}
