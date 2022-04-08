package image.analysis.cloud.app.infra.rpc;

import com.alibaba.fastjson.JSONObject;
import image.analysis.cloud.app.application.AnalysisConfig;
import image.analysis.cloud.app.infra.ResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

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

    /**
     * 远程调用参数：文件名称label, 文件后缀suffix 文件夹load.path, 输出路径out.path,    d-thr, flood, fill, obj-thr(百分比转为小数), stained-thr
     * @return
     */
    public static ResponseWrapper executeTask(long taskId, String taskName, File file, String outputFolderPath, JSONObject param) throws IOException {
        //执行脚本
        Process p = null;
        try {
            String fileName = file.getName();

            //拼接脚本命令及参数
            int lastIndexOf = fileName.lastIndexOf(".");
            String label = fileName.substring(0, lastIndexOf);
            String suffix = fileName.substring(lastIndexOf);
            String loadPath = file.getParentFile().getCanonicalPath();
            String [] commandArray = new String [] {
                    AnalysisConfig.getRscript(), commandFileName,
                    label,
                    suffix,
                    loadPath,
                    outputFolderPath,
                    param.getString("d-thr"),
                    param.getString("flood"),
                    param.getString("fill"),
                    "" + (param.getDouble("obj-thr")/100),
                    param.getString("stained-thr")
            };
            /*String command = String.join(" ", AnalysisConfig.getRscript(), commandFileName,
                    label,
                    suffix,
                    loadPath,
                    outputFolderPath,
                    param.getString("d-thr"),
                    param.getString("flood"),
                    param.getString("fill"),
                    "" + (param.getDouble("obj-thr")/100),
                    param.getString("stained-thr"));*/
            log.info("执行脚本, taskName={},  command[{}]", taskName, commandArray);
            //复制源文件
            FileCopyUtils.copy(file, new File(outputFolderPath + "/" + fileName));
            p = Runtime.getRuntime().exec(commandArray, null, commandDir);
            p.waitFor();
            //读取结果
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream(), "UTF-8"));
            StringBuilder res = new StringBuilder();
            String s = null;
            while ((s = input.readLine()) != null) {
                res.append(s);
            }
            input.close();
            log.info("执行脚本日志【{}】", res);

            //读取异常
            BufferedReader err = new BufferedReader(new InputStreamReader(p.getErrorStream(), "UTF-8"));
            StringBuilder errRes = new StringBuilder();
            String errStr = null;
            while ((errStr = err.readLine()) != null) {
                errRes.append(errStr + "\n");
            }
            err.close();
            if (!errRes.isEmpty()) {
                log.error("执行脚本异常【{}】", errRes);
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
            if (p != null) {
                p.destroy();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        File inputFile = null;
        try {
            inputFile = new ClassPathResource("rcode/test.jpg").getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        new File(outputPath + "/.out_stats").mkdirs();

        JSONObject param = new JSONObject();
        param.put("d-thr", 0.05);
        param.put("flood", "Y");
        param.put("fill", 10);
        param.put("obj-thr", 5);
        param.put("stained-thr", 100);

        ResponseWrapper res = executeTask(1, "task-1", inputFile, outputPath, param);

        System.out.println(JSONObject.toJSONString(res));

    }
}
