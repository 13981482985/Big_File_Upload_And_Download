package com.yang.fileUpload.controller;


import com.yang.fileUpload.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;

@RestController
@Slf4j
public class FileUploadController {
    @Autowired
    private FileUploadService fileUploadService;
    @PostMapping("/csv/upload")
    public void csvFileUpload(@RequestBody MultipartFile file){
        if (file.isEmpty()) {
            log.info("上传文件为空！");
            return;
        }
        Long start = System.currentTimeMillis();
        try {
            /**
             * 内存限制：直接在内存中处理大型文件（尤其是大于几百 MB 或几 GB 的文件）可能导致内存溢出（OutOfMemoryError）。
             * Java 虚拟机的堆内存有大小限制，尤其是在上传文件的场景中，用户上传的文件大小可能远超系统内存的可处理范围。
             * 因此这里可以进一步优化为分片上传 将所有的分片文件合并后在采用流式处理的方式进行数据保存，避免一次性将文件所有数据保存在内存中
             */
            String filename = file.getOriginalFilename().split("\\.")[0];
            if(file.getOriginalFilename().endsWith("csv")){
                Path tempFile = Files.createTempFile("uploaded-", ".csv");
                file.transferTo(tempFile.toFile());
                fileUploadService.completableFutureUploadCsv(tempFile.toFile(), filename);
            } else if(file.getOriginalFilename().endsWith("xlsx")) {
                Path tempFile = Files.createTempFile("uploaded-", ".xlsx");
                file.transferTo(tempFile.toFile());
                fileUploadService.completableFutureUploadExcel(tempFile.toFile(), filename);
            }else if(file.getOriginalFilename().endsWith("xls")){
                Path tempFile = Files.createTempFile("uploaded-", ".xls");
                file.transferTo(tempFile.toFile());
                fileUploadService.completableFutureUploadExcel(tempFile.toFile(), filename);
            }
            Long end = System.currentTimeMillis();
            System.out.println("使用时间："+(double)(end-start)/1000+" 秒");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
