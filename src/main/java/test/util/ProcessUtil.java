package test.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class ProcessUtil {

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
            e.printStackTrace();
            return false;
        }

    }

}
