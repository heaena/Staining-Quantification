package image.analysis.cloud.app.infra.rpc;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.*;
import java.util.Arrays;

public class RemoteAnalysisPlatformService {

    private static Logger log = LoggerFactory.getLogger(RemoteAnalysisPlatformService.class);

    private static final String commandFileName = "command-process-one-image.R";

    private static File commandDir;

    static {
        try {
            commandDir = new ClassPathResource("rcode/" + commandFileName).getFile().getParentFile();
            if (commandDir == null) {
                log.error("没有找到脚本目录");
                System.exit(2);
            }
        } catch (IOException e) {
            log.error("获取脚本目录异常", e);
            System.exit(2);
        }
    }

    public static ResponseWrapper executeTask(long taskId, String taskName, String inputPath, String outputFolderPath, JSONObject param) {
        String roi_fill_thr = param.getString("roi_fill_thr"), stained_thr = param.getString("stained_thr");

        String command = String.join(" ", "Rscript", commandFileName, inputPath, outputFolderPath, roi_fill_thr, stained_thr);
        log.info("执行脚本-> {}", command);
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command, null, commandDir);
            InputStream is = p.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            p.waitFor();
            if (p.exitValue() != 0) {
                //说明命令执行失败
                return ResponseWrapper.fail();
            }

            StringBuilder res = new StringBuilder();
            String s = null;
            while ((s = reader.readLine()) != null) {
                res.append(s);
            }
            log.info("执行脚本日志【{}】", res.toString());
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
        param.put("roi_fill_thr", "7");
        param.put("stained_thr", "0.7");

        executeTask(1, "task-1", inputPath, outputPath, param);

    }
}
