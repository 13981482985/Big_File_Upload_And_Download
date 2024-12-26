package com.yang.fileUpload.service;

import javax.servlet.http.HttpServletResponse;

public interface FileDownloadService {
    void streamDownload(HttpServletResponse httpServletResponse, String fileName);
}
