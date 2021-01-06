package com.activiti.controller;

import com.activiti.util.AjaxResponse;
import com.activiti.util.GlobalConfig;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipInputStream;

@RestController
@RequestMapping("/processDefinition")
public class ProcessDefinitionController {

    @Autowired
    private RepositoryService repositoryService;


    //获取流程定义列表信息
    @GetMapping(value = "/getDefinitions")
    public AjaxResponse getDefinitions() {
        try {
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
            for (ProcessDefinition pd : list) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("processDefinitionID", pd.getId());
                map.put("name", pd.getName());
                map.put("key", pd.getKey());
                map.put("resourceName", pd.getResourceName());
                map.put("deploymentID", pd.getDeploymentId());
                map.put("version", pd.getVersion());
                listMap.add(map);
            }
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, listMap);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }

    }

    //分别使用zip文件或者.bpmn文件来上传流程文件
    @PostMapping(value = "uploadStreamAndDeployment")
    public AjaxResponse uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile) {
        //获取上传的文件名
        String fileName = multipartFile.getOriginalFilename();
        try {
            InputStream inputStream = multipartFile.getInputStream();
            String ext = FilenameUtils.getExtension(fileName);
            Deployment deployment = null;
            if (ext.equals("zip")) {
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                deployment = repositoryService.createDeployment()
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            } else {
                deployment = repositoryService.createDeployment()
                        .addInputStream(fileName, inputStream)
                        .name("流程部署名称可通过接口传递现在写死")
                        .deploy();
            }
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, deployment.getName() + ":" + deployment.getId());
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }

    }

    //使用在线绘制的方式上传流程文件
    @PostMapping(value = "addDeploymentByString")
    public AjaxResponse addDeploymentByString(@RequestParam("stringBPMN") String stringBPMN) {
        try {
            Deployment deployment = repositoryService.createDeployment()
                    .addString("CreateWithBPMNJS.bpmn", stringBPMN)
                    .name("不知道在哪显示的部署名称")
                    .deploy();
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, deployment.getName() + ":" + deployment.getId());
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    /**
     * 获取流程定义xml
     */
    @GetMapping(value = "")
    public void getProcessDefineXML(HttpServletResponse response,
                                    @RequestParam("deploymentId") String deploymentId,
                                    @RequestParam("resourceName") String resourceName) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            OutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @GetMapping(value = "/getDeployments")
    public AjaxResponse getDeployments() {
        try {
            List<HashMap<String, Object>> listMap = new ArrayList<>();
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for (Deployment dep : list) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("id", dep.getId());
                hashMap.put("name", dep.getName());
                hashMap.put("deploymentTime", dep.getDeploymentTime());
            }
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, listMap);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    @GetMapping(value = "")
    public AjaxResponse delDefinition(@RequestParam("pdID") String pdID) {
        try {
            //true表示删除所有的历史
            repositoryService.deleteDeployment(pdID, true);
            int status = GlobalConfig.ResponseCode.SUCCESS.getCode();
            String desc = GlobalConfig.ResponseCode.SUCCESS.getDesc();
            return AjaxResponse.AjaxData(status, desc, null);
        } catch (Exception e) {
            int status = GlobalConfig.ResponseCode.ERROR.getCode();
            String desc = GlobalConfig.ResponseCode.ERROR.getDesc();
            return AjaxResponse.AjaxData(status, desc, e.getMessage());
        }
    }

    @PostMapping(value = "/upload")
    public AjaxResponse upload(HttpServletRequest request, @RequestParam("processFile") MultipartFile multipartFile) {

        if (multipartFile.isEmpty()) {
            System.out.println("文件为空");
        }
        String fileName = multipartFile.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名
        String filePath = GlobalConfig.BPMN_PathMapping; // 上传后的路径

        //本地路径格式转上传路径格式
        filePath = filePath.replace("\\", "/");
        filePath = filePath.replace("file:", "");

        // String filePath = request.getSession().getServletContext().getRealPath("/") + "bpmn/";
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        File file = new File(filePath + fileName);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            multipartFile.transferTo(file);
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return AjaxResponse.AjaxData(GlobalConfig.ResponseCode.SUCCESS.getCode(),
                GlobalConfig.ResponseCode.SUCCESS.getDesc(), fileName);
    }

}
