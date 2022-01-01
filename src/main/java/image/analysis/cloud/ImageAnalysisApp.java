package image.analysis.cloud;


import image.analysis.cloud.service.RService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

@SpringBootApplication
public class ImageAnalysisApp {

    public static void main(String[] args) {
        JFrame jf = new JFrame(ImageAnalysisApp.class.getSimpleName());
        jf.setSize(700, 500);
        jf.setLocationRelativeTo(null);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        //安装依赖按钮
        addInstallPackagesBtn(panel);
        addInstalledPackagesBtn(panel);
        //首页按钮
        addHomeBtn(panel);
        //日志区域
        addLogTextArea(panel);

        jf.setContentPane(panel);
        jf.setVisible(true);

        //启动web服务
        new Thread(() -> SpringApplication.run(ImageAnalysisApp.class, args)).start();

    }

    /**在面板中的索引*/
    public static final int INDEX_INSTALL_PACKAGES_BTN = 0;
    public static final int INDEX_INSTALLED_PACKAGES_BTN = 1;
    public static final int INDEX_HOME_BTN = 2;
    public static final int INDEX_LOG_AREA = 3;

    private static JTextArea log;

    /**
     * 点击按钮，安装R依赖
     * @param panel 面板
     * @return
     */
    private static void addInstallPackagesBtn(JPanel panel) {
        final JButton installPackagesBtn = new JButton("安装R依赖");
        installPackagesBtn.addActionListener(e -> {
            cleanAndBeginLog("install.packages");
            new Thread(() -> RService.installPackages(getLog())).start();
        });
        panel.add(installPackagesBtn, INDEX_INSTALL_PACKAGES_BTN);
    }

    /**
     * 点击按钮，查询已安装R依赖
     * @param panel 面板
     * @return
     */
    private static void addInstalledPackagesBtn(JPanel panel) {
        final JButton installedPackagesBtn = new JButton("查询已安装R依赖");
        installedPackagesBtn.addActionListener(e -> {
            cleanAndBeginLog("installed.packages");
            new Thread(() -> RService.installedPackages(getLog())).start();
        });
        panel.add(installedPackagesBtn, INDEX_INSTALLED_PACKAGES_BTN);
    }

    /**
     * 获取日志组件
     * @return
     */
    private static JTextArea getLog() {
        return log;
    }

    /**
     * 清除日志
     */
    private static void cleanAndBeginLog(String title) {
        log.setText("> " + title + "\n================================\n");
    }


    /**
     * 点击按钮，安装R依赖
     * @param panel 面板
     * @return
     */
    private static void addLogTextArea(JPanel panel) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(28, 209, 1000, 100);
        log = new JTextArea(50,100);
        log.setLineWrap( true );
        log.setWrapStyleWord( true );
        log.setEditable(false);
        scrollPane.setViewportView(log);

        panel.add(scrollPane, INDEX_LOG_AREA);
    }

    /**
     * 点击按钮，进入首页
     * @param panel 面板
     * @return
     */
    private static void addHomeBtn(JPanel panel) {
        final JButton homeBtn = new JButton("首页");
        homeBtn.addActionListener(e -> openBrowse("/"));
        panel.add(homeBtn, INDEX_HOME_BTN);
    }

    public static void openBrowse(String path) {
        URI uri = null;
        try {
            uri = new URL("http://localhost").toURI();
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;

            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                desktop.browse(uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
