package image.analysis.cloud.app.infra.repo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import image.analysis.cloud.app.application.domain.model.AnalysisTask;
import image.analysis.cloud.app.application.domain.repo.AnalysisTaskRepo;
import image.analysis.cloud.app.infra.repo.mapper.AnalysisTaskMapper;
import org.springframework.stereotype.Repository;

@Repository
public class AnalysisTaskRepoImpl extends ServiceImpl<AnalysisTaskMapper, AnalysisTask> implements AnalysisTaskRepo {
}
