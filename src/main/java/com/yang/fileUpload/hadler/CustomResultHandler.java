package com.yang.fileUpload.hadler;

import com.yang.fileUpload.utils.DownloadProcessor;
import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

import java.util.LinkedHashMap;
import java.util.Objects;

public class CustomResultHandler implements ResultHandler {

    private final DownloadProcessor downloadProcessor;

    public CustomResultHandler(DownloadProcessor downloadProcessor) {
        super();
        this.downloadProcessor = downloadProcessor;
    }

    @Override
    public void handleResult(ResultContext resultContext) {
        // 获取流式结果
        LinkedHashMap<String, Object> map = (LinkedHashMap)resultContext.getResultObject();
        // 处理流式结果
        downloadProcessor.processData(map);
    }
}
