package org.onetwo.boot.core.cleanup;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.onetwo.boot.core.config.BootSiteConfig.UploadConfig;
import org.onetwo.common.log.JFishLoggerFactory;
import org.slf4j.Logger;

public class LocalFileCleanup implements FileStoreCleanup {
	private final Logger logger = JFishLoggerFactory.getLogger(getClass());
	private UploadConfig uploadConfig;
	
	public LocalFileCleanup(UploadConfig uploadConfig) {
		super();
		this.uploadConfig = uploadConfig;
	}

	@Override
	public void cleanup() {
		File directory = new File(uploadConfig.getFileStorePath());
        if (!directory.exists()) {
			logger.warn("local cleanup ignore : store file is not exists");
            return;
        }
        if (!directory.isDirectory()) {
			logger.warn("local cleanup ignore : store path is not a directory");
            return;
        }

        long expiredDaysInMillis = TimeUnit.DAYS.toMillis(uploadConfig.getCleanup().getExpiredInDays());
        cleanFiles(directory, expiredDaysInMillis);
	}
	
	/****
	 * 
	 * @param directory
	 * @param expiredInDays 多少天之前为过期
	 */
	public void cleanFilesWithExpiredDays(File directory, int expiredInDays) {
		long expiredDaysInMillis = TimeUnit.DAYS.toMillis(expiredInDays);
		cleanFiles(directory, expiredDaysInMillis);
	}
	
    private void cleanFiles(File directory, long expiredDaysInMillis) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                cleanFiles(file, expiredDaysInMillis); // 递归处理子目录
            } else {
                if (isExpiredFile(file, expiredDaysInMillis)) {
                    if (file.delete()) {
                        logger.info("deleted file: " + file.getAbsolutePath());
                    } else {
                        logger.error("can not delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

    private boolean isExpiredFile(File file, long expiredDaysInMillis) {
        try {
            Path filePath = file.toPath();
            BasicFileAttributes attrs = Files.readAttributes(filePath, BasicFileAttributes.class);
            // 最后修改时间
//            long fileAgeInMillis = new Date().getTime() - attrs.lastModifiedTime().toMillis();
            // 创建时间
            long fileAgeInMillis = new Date().getTime() - attrs.creationTime().toMillis();
            return fileAgeInMillis >= expiredDaysInMillis;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
