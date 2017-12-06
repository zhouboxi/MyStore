package com.jxlg.app.store.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author zhouboxi
 * @create 2017-12-02 20:23
 **/
public interface IFileService {

    String upload(MultipartFile file, String path);
}
