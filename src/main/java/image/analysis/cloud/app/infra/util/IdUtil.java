package image.analysis.cloud.app.infra.util;

import java.util.UUID;

public class IdUtil {
    public static String uuid() {
        return UUID.randomUUID().toString();
    }
}
