package com.mybucketpet.service.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Component
@Slf4j
public class FileService {

    @Value("${bucket.upload.file}")
    private String fileDir;

    private String getFullPath(String fileName) {
        return fileDir + fileName;
    }

    public String saveFile(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return null;
        }

        String originFileName = file.getOriginalFilename();
        String extension = extractExtension(originFileName);
        String saveFileName = UUID.randomUUID().toString() + file.getSize() + "." + extension;
        // 저장할 폴더 경로 생성
        createSaveDirectory();
        // 파일 저장
        file.transferTo(new File(getFullPath(saveFileName)));
        // 저장된 파일 명 반환
        return saveFileName;
    }

    public void deleteFile(String saveFileName) {
        File file = new File(getFullPath(saveFileName));
        if (file.exists()) {
            if (file.delete()) {
                log.info("파일이 정삭적으로 삭제되었습니다. FileName = {}", saveFileName);
            } else {
                log.error("파일 삭제에 실패하였습니다. FileName = {}", saveFileName);
            }
        } else {
            log.error("이미 삭제된 썸네일 이미지 파일입니다. FileName = {}", saveFileName);
        }
    }

    private void createSaveDirectory() {
        File saveDirectory = new File(fileDir);
        if (!saveDirectory.exists()) {
            saveDirectory.mkdirs();
        }
    }

    /**
     * 파일의 확장자만 추출
     * @param fileName : 원본 파일명
     * @return : 확장자
     */
    private String extractExtension(String fileName) {
        int pos = fileName.lastIndexOf(".");

        return fileName.substring(pos + 1);
    }

}
