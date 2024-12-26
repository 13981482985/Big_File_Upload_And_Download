package com.yang.fileUpload.service.impl;

import com.yang.fileUpload.hadler.CustomResultHandler;
import com.yang.fileUpload.mapper.FileMapper;
import com.yang.fileUpload.service.FileDownloadService;
import com.yang.fileUpload.utils.DownloadProcessor;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Service
public class FileDownloadServiceImpl implements FileDownloadService {
    @Autowired
    private  SqlSessionTemplate sqlSessionTemplate;
    @Autowired
    private FileMapper fileMapper;
    /**
     * stream读数据写文件方式
     * @param httpServletResponse
     * @throws IOException
     */
    public void streamDownload(HttpServletResponse httpServletResponse,String tableName) {
        try {
            List<String> filedList = fileMapper.getFiledList(tableName);
            HashMap<String, Object> param = new HashMap<>();
            param.put("tableName", tableName);
            param.put("fields", filedList);
            DownloadProcessor downloadProcessor = new DownloadProcessor(httpServletResponse,tableName);
            CustomResultHandler customResultHandler = new CustomResultHandler(downloadProcessor);
            // 流式查询 & 流式处理
            sqlSessionTemplate.select(
                    "com.yang.fileUpload.mapper.FileMapper.streamSelect",param, customResultHandler);
            downloadProcessor.finalizeDownload();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
