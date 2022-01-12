package image.analysis.cloud.app.entrypoint.gui;

import image.analysis.cloud.app.ImageAnalysisApp;
import image.analysis.cloud.app.application.service.RService;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.awt.*;
import java.net.URI;
import java.net.URL;

@Component
public class GuiApp extends JFrame {

    public void start() {
        init();
    }
    public void init() {
        setTitle(ImageAnalysisApp.class.getSimpleName());
        setSize(700, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();

        //安装依赖按钮
//        addInstallPackagesBtn(panel);
        addInstalledPackagesBtn(panel);
        //首页按钮
        addHomeBtn(panel);
        //日志区域
        addLogTextArea(panel);

        setContentPane(panel);
        setVisible(true);

    }

    /**在面板中的索引*/
    final int INDEX_INSTALL_PACKAGES_BTN = 0;
    final int INDEX_INSTALLED_PACKAGES_BTN = 0;
    final int INDEX_HOME_BTN = 1;
    final int INDEX_LOG_AREA = 2;

    private static JTextArea log;

    /**
     * 点击按钮，安装R依赖
     * @param panel 面板
     * @return
     */
    /*private void addInstallPackagesBtn(JPanel panel) {
        final JButton installPackagesBtn = new JButton("安装R依赖");
        installPackagesBtn.addActionListener(e -> {
            cleanAndBeginLog("install.packages");
            new Thread(() -> RService.installPackages(getLog())).start();
        });
        panel.add(installPackagesBtn, INDEX_INSTALL_PACKAGES_BTN);
    }*/

    /**
     * 点击按钮，查询已安装R依赖
     * @param panel 面板
     * @return
     */
    private void addInstalledPackagesBtn(JPanel panel) {
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
    private JTextArea getLog() {
        return log;
    }

    /**
     * 清除日志
     */
    private void cleanAndBeginLog(String title) {
        log.setText("> " + title + "\n================================\n");
    }


    /**
     * 点击按钮，安装R依赖
     * @param panel 面板
     * @return
     */
    private void addLogTextArea(JPanel panel) {
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
    private void addHomeBtn(JPanel panel) {
        final JButton homeBtn = new JButton("首页");
        homeBtn.addActionListener(e -> openBrowse("/"));
        panel.add(homeBtn, INDEX_HOME_BTN);
    }

    private static void openBrowse(String path) {
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
