package com.yang.fileUpload.controller;


import com.yang.fileUpload.service.FileDownloadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
public class FileDownloadController {

    @Autowired
    private FileDownloadService fileDownloadService;

    @GetMapping("/file/download")
    public void fileDownload(HttpServletResponse  httpServletResponse, @RequestParam("tableName") String tableName){
        System.out.println("开始下载.....");
        long start = System.currentTimeMillis();
        fileDownloadService.streamDownload(httpServletResponse,tableName);
        System.out.println("下载时间(秒)："+(float)(System.currentTimeMillis()-start)/1000);
    }
}
