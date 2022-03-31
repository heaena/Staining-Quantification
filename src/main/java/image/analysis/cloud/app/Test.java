package image.analysis.cloud.app;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Test {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        String path = null;

//设置界面风格

        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        JFileChooser jdir = new JFileChooser();

//设置选择路径模式

        jdir.setFileSelectionMode(JFileChooser.FILES_ONLY);

//过滤文件类型

        FileNameExtensionFilter filter = new FileNameExtensionFilter(

                "Image(*.jpg)","jpg");

        jdir.setFileFilter(filter);

//设置对话框标题

        jdir.setDialogTitle("请选择文件路径");

        if (JFileChooser.APPROVE_OPTION == jdir.showOpenDialog(null)) {//用户点击了确定

            path = jdir.getSelectedFile().getAbsolutePath();//取得路径选择

        }

        System.out.println(path);
    }
}
