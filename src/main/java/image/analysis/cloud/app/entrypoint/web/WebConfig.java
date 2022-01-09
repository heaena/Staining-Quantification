package image.analysis.cloud.app.entrypoint.web;

import image.analysis.cloud.app.application.AnalysisConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter(WebProperties.class)
public class WebConfig implements InitializingBean {

    @Autowired
    private WebProperties webProperties;

    /**
     * 设置静态资源路径
     */
    @Override
    public void afterPropertiesSet() {
        webProperties.getResources().setStaticLocations(new String[]{
                "classpath:/resources/dist",
                "file:" + AnalysisConfig.getImgAnalysisPath()
        });
    }

}
