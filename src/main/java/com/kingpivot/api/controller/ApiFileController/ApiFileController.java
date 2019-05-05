package com.kingpivot.api.controller.ApiFileController;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.kingpivot.base.config.Config;
import com.kingpivot.protocol.ApiBaseController;
import com.kingpivot.protocol.MessageHeader;
import com.kingpivot.protocol.MessagePacket;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.qiniu.storage.Configuration;

@RequestMapping("/api")
@RestController
@Api(description = "文件管理接口")
public class ApiFileController extends ApiBaseController {

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "file", value = "文件对象", dataType = "file")})
    @RequestMapping(value = "/uploadFile")
    public MessagePacket uploadFile(@RequestParam MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return MessagePacket.newFail(MessageHeader.Code.illegalParameter, "文件为空");
        }
        List<String> list = new LinkedList<>();
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //生成上传凭证，然后准备上传
        Auth auth = Auth.create(Config.QINIU_ACCESSKEY, Config.QINIU_SECRETKEY);
        String upToken = auth.uploadToken(Config.QINIU_BUCKETNAME);
        byte[] uploadBytes = getBytes(file);
        Response response = uploadManager.put(uploadBytes, null, upToken);
        //解析上传成功的结果
        DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
        StringBuilder uploadUrl = new StringBuilder(Config.QINIU_LOOKHEAD);
        uploadUrl.append(putRet.key);
        list.add(uploadUrl.toString());
        Map<String, Object> rsMap = Maps.newHashMap();
        rsMap.put("data", list);
        return MessagePacket.newSuccess(rsMap, "uploadFile success!");
    }

    //获得指定文件的byte数组
    private byte[] getBytes(MultipartFile multipartFile) {
        byte[] buffer = null;
        try {
            InputStream fis = multipartFile.getInputStream();
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }
}
