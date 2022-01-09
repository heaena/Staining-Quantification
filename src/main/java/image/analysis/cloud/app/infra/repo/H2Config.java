package image.analysis.cloud.app.infra.repo;

import com.zaxxer.hikari.HikariDataSource;
import image.analysis.cloud.app.application.AnalysisConfig;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AutoConfigureAfter({AnalysisConfig.class})
public class H2Config {

    @Bean
    public HikariDataSource getDataSource(AnalysisConfig analysisConfig) throws Exception {
        HikariDataSource hikariDataSource = DataSourceBuilder.create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:file:" + AnalysisConfig.getImgAnalysisH2Path())
                .username("sa")
                .password("sa")
                .type(HikariDataSource.class).build();
        return hikariDataSource;
    }

}
