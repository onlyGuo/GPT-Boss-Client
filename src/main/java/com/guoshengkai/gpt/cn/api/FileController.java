package com.guoshengkai.gpt.cn.api;

import com.guoshengkai.gpt.cn.conf.NoLogin;
import com.guoshengkai.gpt.cn.core.Global;
import com.guoshengkai.gpt.cn.core.util.DateUtil;
import com.guoshengkai.gpt.cn.core.util.FileUtil;
import com.guoshengkai.gpt.cn.exception.ValidationException;
import com.guoshengkai.gpt.cn.util.ThreadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Map;

@RestController
@RequestMapping("api/v1/file")
public class FileController {

    @NoLogin
    @SneakyThrows
    @PostMapping("upload")
    public Map<String, String> upload(MultipartFile file) {
        if (!Global.fun.isEnableGuestUpload() && ThreadUtil.getUserEntity() == null){
            throw new ValidationException("该站点不允许游客上传文件，请先登录");
        }
        String key = DateUtil.formatPramm("yyyy/MM/dd") + "/" + ThreadUtil.getUserId() + "/" + file.getOriginalFilename();
        FileUtil.writeFile(file.getBytes(), new File("data/files/" + key));
        return Map.of("fileKey", key);
    }

    @NoLogin
    @GetMapping("display/**")
    public void display(HttpServletResponse response, HttpServletRequest request){
        String[] split = request.getRequestURI().split("/api/v1/file/display/");
        if (split.length != 2){
            return;
        }
        String fileKey = split[1];
//        filesService.download(fileKey, response);
        try (OutputStream out = response.getOutputStream()){
            out.write(FileUtil.readFile2Byte(new File("data/files/" + URLDecoder.decode(fileKey, "UTF-8"))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
