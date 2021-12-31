package test.service;

import test.util.ProcessUtil;

import javax.swing.*;

public class RService {

    /**
     * 安装R依赖
     * @param log 日志显示
     * @return
     */
    public static boolean installPackages(JTextArea log) {
        return execAndPrint("script/install.packages", log);
    }

    private static boolean execAndPrint(String scriptFile, JTextArea log) {
        try {
            String scriptPath = RService.class.getClassLoader().getResource(scriptFile).getPath();
            return ProcessUtil.exec(scriptPath, line -> {
                if (log != null) {
                    log.append(line + "\n");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查询已安装的依赖
     * @param log 日志显示
     * @return
     */
    public static boolean installedPackages(JTextArea log) {
        return execAndPrint("script/installed.packages", log);
    }
}
