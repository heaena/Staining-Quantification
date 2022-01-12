package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.AnalysisConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({WebProperties.class, ServerProperties.class})
public class WebConfig implements InitializingBean {

    @Autowired
    private WebProperties webProperties;

    public static final String restApiContextPath = "/api";

    @Override
    public void afterPropertiesSet() {
        //设置静态资源路径
        webProperties.getResources().setStaticLocations(new String[]{"classpath:/static", "file:" + AnalysisConfig.getImgAnalysisWorkspacePath() + "/"});
    }

}
