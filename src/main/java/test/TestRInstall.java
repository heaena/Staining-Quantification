package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class TestRInstall {
    public static void main(String[] args) {
        try {
            Process process = Runtime.getRuntime().exec("sh /Users/zhangkai/IdeaProjects/jpackage-test/test.sh");
            BufferedReader inr = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            String line = null;
            while ((line = inr.readLine()) != null) {
                System.out.println(line);
            }
            inr.close();
            //返回值为0表示我们调用成功
            int re = process.waitFor();
            System.out.println(re);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            TimeUnit.HOURS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
