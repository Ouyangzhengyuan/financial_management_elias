package pers.elias.financial_management.component;

/**
 * 账本头像上传组件
 */

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

@Component
public class FileUpload {
    private String filename;

    public File getDestFile(String dirPath, MultipartFile file){
        //创建文件目录
        File dirFile = new File(dirPath);
        //判断目录是否存在
        if (!dirFile.exists()) {
            boolean success = dirFile.mkdirs();
        }
        String originalFileName = file.getOriginalFilename();
        assert originalFileName != null;
        int beginIndex = originalFileName.lastIndexOf(".");
        String suffix = "";
        if (beginIndex != -1) {
            suffix = originalFileName.substring(beginIndex);
        }
        //设置唯一标识文件名
        filename = UUID.randomUUID().toString() + suffix;
        return new File(dirFile, filename);
    }

    public String getFilename() {
        return filename;
    }
}
