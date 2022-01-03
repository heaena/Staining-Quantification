package image.analysis.cloud.app.infra.repo.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import image.analysis.cloud.app.application.domain.model.FileSystem;
import image.analysis.cloud.app.application.domain.repo.FileSystemRepo;
import image.analysis.cloud.app.infra.repo.mapper.FileSystemMapper;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepoImpl extends ServiceImpl<FileSystemMapper, FileSystem> implements FileSystemRepo {
}
