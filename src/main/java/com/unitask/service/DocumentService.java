package com.unitask.service;

import com.unitask.dto.DocumentPageRequest;
import com.unitask.util.PageWrapperVO;
import jakarta.servlet.http.HttpServletResponse;

public interface DocumentService {

    PageWrapperVO getListing(DocumentPageRequest documentPageRequest);
    void downloadFile(String path, HttpServletResponse response);

}
