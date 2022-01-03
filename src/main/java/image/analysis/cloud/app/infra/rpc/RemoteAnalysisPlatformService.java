package image.analysis.cloud.app.infra.rpc;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class RemoteAnalysisPlatformService {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Resource
    private RestTemplate restTemplate;

    @Value("${web.analysis-platform.rscript-dir}")
    private String rscriptDir;

    public ResponseWrapper startTask(String taskId, String inputFilePath, String outputFilePath, JSONObject param) {
        String ROI_fill_thr = param.getString("roi_fill_thr"), stained_thr = param.getString("stained_thr");
        String command = String.join(" ", "Rscript command-batch-processing.R", inputFilePath, outputFilePath, ROI_fill_thr, stained_thr);
        log.info("执行脚本-> {}", command);
        Process p = null;
        try {
            p = Runtime.getRuntime().exec(command, null, new File(rscriptDir));
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

}
