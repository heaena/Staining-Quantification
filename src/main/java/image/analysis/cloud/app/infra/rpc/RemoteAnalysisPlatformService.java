package image.analysis.cloud.app.infra.rpc;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

public class RemoteAnalysisPlatformService {

    private static Logger log = LoggerFactory.getLogger(RemoteAnalysisPlatformService.class);

    private static String commandFilePath = null;
    private static final String commandFileName = "command-process-one-image.R";

    private static File commandDir;

    static {
        try {
            commandDir = new ClassPathResource("/rcode/").getFile();
//            File absoluteFile = ResourceUtils.getFile("/rcode/").getAbsoluteFile();
//            File canonicalFile = ResourceUtils.getFile("/rcode/").getCanonicalFile();
//            commandFilePath = ResourceUtils.getFile("/rcode/").getAbsolutePath() + "/" + commandFileName;
            if (commandDir == null) {
                log.error("没有找到脚本目录");
                System.exit(2);
            }
        } catch (IOException e) {
            log.error("获取脚本目录异常", e);
            System.exit(2);
        }
    }

    public static ResponseWrapper executeTask(long taskId, String taskName, String inputPath, String outputFolderPath, JSONObject param, JTextArea jTextArea) {
        String command = String.join(" ", "Rscript", commandFileName, inputPath, outputFolderPath, param.toJSONString());
//        String command = String.join(" ", "Rscript", commandDir.getCanonicalPath() + "/" + commandFileName, inputPath, outputFolderPath, param.toJSONString());
        log.info("执行脚本-> {}", command);
        Process p = null;
        try {
//            p = Runtime.getRuntime().exec(command);
            p = Runtime.getRuntime().exec(command, null, commandDir);
            p.waitFor();
            //读取结果
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
            StringBuilder res = new StringBuilder();
            String s = null;
            while ((s = input.readLine()) != null) {
                res.append(s);
                if (jTextArea != null) {
                    jTextArea.append(s + "\n");
                }
            }
            input.close();
            log.info("执行脚本日志【{}】", res);

            //读取异常
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
            StringBuilder errRes = new StringBuilder();
            String errStr = null;
            while ((errStr = err.readLine()) != null) {
                errRes.append(errStr);
                if (jTextArea != null) {
                    jTextArea.append(errStr + "\n");
                }
            }
            err.close();
            if (!errRes.isEmpty()) {
                log.error("执行脚本异常【{}】", errRes);
                if (jTextArea != null) {
                    jTextArea.append("执行异常，请联系管理员！");
                }
                return ResponseWrapper.fail();
            }

            if (p.exitValue() != 0) {
                //说明命令执行失败
                return ResponseWrapper.fail();
            }

            return ResponseWrapper.success();
        } catch (Exception e) {
            log.error("执行脚本异常", e);
            return ResponseWrapper.fail();
        } finally {
            p.destroy();
        }
    }

    public static void main(String[] args) {
        File inputFile = null;
        try {
            inputFile = new ClassPathResource("rcode/test.jpg").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String inputPath = inputFile.getPath();
        String outputPath = inputFile.getParentFile().getParentFile().getParentFile().getParentFile().getPath() + "/temp";

        //clean outputFolder
        File outputFolder = new File(outputPath);
        File[] files = outputFolder.listFiles();
        if (files != null) {
            Arrays.stream(files).forEach(item -> {
                item.delete();
            });
        }
        outputFolder.deleteOnExit();
        outputFolder.mkdir();

        JSONObject param = new JSONObject();
        param.put("ROI_fill_thr", "7");
        param.put("stained_thr", "0.7");

        executeTask(1, "task-1", inputPath, outputPath, param, null);

    }
}
