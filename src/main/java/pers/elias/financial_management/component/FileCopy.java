package pers.elias.financial_management.component;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileCopy {
    public boolean copyFile(String targetFilePath, File fileSource) {
        try {
            File targetFile = new File(targetFilePath, fileSource.getName());
            if (!targetFile.exists()) {
                boolean success = targetFile.createNewFile();
            }
            FileInputStream  fis = new FileInputStream(fileSource);
            FileOutputStream fos = new FileOutputStream(targetFile);
            int length;
            byte[] bytes = new byte[1024];
            while((length = fis.read(bytes)) != -1){
                fos.write(bytes, 0, length);
            }
            fis.close();
            fos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
