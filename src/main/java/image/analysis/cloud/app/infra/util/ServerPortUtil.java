package image.analysis.cloud.app.infra.util;

import org.h2.util.NetUtils;

import java.net.InetAddress;
import java.net.Socket;
import java.util.Random;

public class ServerPortUtil {

    private static final int MAX = 65535;

    private static final int MIN = 2000;

    public static int getAvailablePort() {
        Random random = new Random();
        int port = random.nextInt(MAX) % (MAX - MIN + 1) + MIN;
        boolean using = isLocalPortUsing(port);
        if (using) {
            return getAvailablePort();
        } else {
            return port;
        }
    }

    public static boolean isLocalPortUsing(int port){
        boolean flag = true;
        try {
            flag = isPortUsing("127.0.0.1", port);
        } catch (Exception e) {
        }
        return flag;
    }

    public static boolean isPortUsing(String host,int port) {
        boolean flag = false;
        try {
            InetAddress theAddress = InetAddress.getByName(host);
            Socket socket = new Socket(theAddress,port);
            flag = true;
        } catch (Exception e) {

        }
        return flag;
    }
}
