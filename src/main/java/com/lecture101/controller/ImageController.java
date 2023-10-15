package com.lecture101.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class ImageController {

    @GetMapping("/images/{imageName}")
    @ResponseBody
    public byte[] getImage(@PathVariable String imageName, ByteArrayOutputStream IOUtils) throws IOException {
        Resource resource;
        try {
            resource = new ClassPathResource("static/" + imageName + ".jpg");
        } catch (Exception e) {
            resource = new ClassPathResource("static/images/noimg.jpg");
        }
        InputStream in = resource.getInputStream();
        return IOUtils.toByteArray();
    }
}
