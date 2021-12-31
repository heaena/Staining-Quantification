package test.util;

import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.function.Consumer;

public class ProcessUtil {

    public static boolean exec(String command, Consumer< String > consumer) {
        try {
            Process process = Runtime.getRuntime().exec(command);
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
