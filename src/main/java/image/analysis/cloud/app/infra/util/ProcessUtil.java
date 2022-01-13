package image.analysis.cloud.app.infra.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.util.function.Consumer;

public class ProcessUtil {

    private static final Logger log = LoggerFactory.getLogger(ProcessUtil.class);

    private static final String systemCommand = getSystemCommand();

    public static String getSystemCommand() {
        String os = System.getProperty("os.name");
        if(os.toLowerCase().startsWith("win")){
            return "cmd";
        } else {
            return "sh";
        }
    }

    /**
     * 执行脚本
     * @param scriptPath 脚本路径
     * @param consumer 控制台输出回调
     * @return
     */
    public static boolean exec(String scriptPath, Consumer< String > consumer) {
        try {
            Process process = Runtime.getRuntime().exec(systemCommand + " " + scriptPath);
            BufferedReader inr = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = inr.readLine()) != null) {
                consumer.accept(line);
            }
            inr.close();
            //返回值为0表示调用成功
            int re = process.waitFor();
            return re == 0;
        } catch (Exception e) {
            consumer.accept(e.toString());
            log.error("脚本执行异常", e);
            return false;
        }

    }

    public static void main(String[] args) {
        String c = "Rscript -e 'installed.packages()'";
        try {
            Process process = Runtime.getRuntime().exec(c);

            int re = process.waitFor();
            SequenceInputStream sequenceInputStream = new SequenceInputStream(process.getInputStream(), process.getErrorStream());
            BufferedReader errorr = new BufferedReader(new InputStreamReader(sequenceInputStream,"UTF-8"));
            String errorLine = null;
            while ((errorLine = errorr.readLine()) != null) {
                System.out.println(errorLine);
            }
            errorr.close();
            //返回值为0表示调用成功

            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
