package image.analysis.cloud.app.infra.repo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import image.analysis.cloud.app.application.domain.model.AnalysisTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AnalysisTaskMapper extends BaseMapper<AnalysisTask> {
}
