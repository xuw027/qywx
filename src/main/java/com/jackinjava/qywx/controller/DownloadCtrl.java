package com.jackinjava.qywx.controller;

import com.alibaba.excel.EasyExcel;
import com.jackinjava.qywx.model.UserCompare;
import com.jackinjava.qywx.service.UserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/download")
public class DownloadCtrl {
    @Autowired
    private UserDetailService userDetailService;

    @GetMapping("excel")
    public void download(HttpServletResponse response) throws IOException, ExecutionException, InterruptedException {
        List<UserCompare> data = userDetailService.getUser();
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("测试", "UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), UserCompare.class).sheet("模板").doWrite(data);
    }
}
