package image.analysis.cloud.app.application.service;

import image.analysis.cloud.app.infra.util.ProcessUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.swing.*;

public class RService {

    private static Logger loger = LoggerFactory.getLogger(RService.class);

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
            String scriptPath = new ClassPathResource(scriptFile).getFile().getPath();
            loger.info(scriptPath);
            return ProcessUtil.exec(scriptPath, line -> {
                if (log != null) {
                    log.append(line + "\n");
                }
            });
        } catch (Exception e) {
            log.append(e.toString());
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
