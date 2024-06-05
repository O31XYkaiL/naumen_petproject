package com.example.naumenProject.repositories;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface Error {
    @RequestMapping("/error")
    @ResponseBody
    String error(HttpServletRequest request);


    String getErrorPath();
}
