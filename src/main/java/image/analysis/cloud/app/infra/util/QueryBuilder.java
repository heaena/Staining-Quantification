package image.analysis.cloud.app.infra.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.util.StringUtils;

public class QueryBuilder {
    private QueryWrapper queryWrapper;

    public QueryBuilder(BaseQuery baseQuery) {
        this.queryWrapper = new QueryWrapper();
    }

    public QueryBuilder() {
        this.queryWrapper = new QueryWrapper();
    }

    public static QueryBuilder init(BaseQuery baseQuery) {
        return new QueryBuilder(baseQuery);
    }

    public static QueryBuilder init() {
        return new QueryBuilder();
    }

    public QueryBuilder eq(String col, Long val) {
        if (StringUtils.isEmpty(val)) {
            return this;
        }
        if (val > 0) {
            queryWrapper.eq(col, val);
        }
        return this;
    }

    public QueryBuilder eq(String col, Object val) {
        if (StringUtils.isEmpty(val)) {
            return this;
        }
        queryWrapper.eq(col, val);
        return this;
    }

    public QueryBuilder ne(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.ne(col, val);
        }
        return this;
    }

    public QueryBuilder le(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.le(col, val);
        }
        return this;
    }

    public QueryBuilder ge(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.ge(col, val);
        }
        return this;
    }

    public QueryBuilder like(String col, Object val) {
        if (!StringUtils.isEmpty(val)) {
            queryWrapper.like(col, val);
        }
        return this;
    }

    public QueryBuilder orderByAsc(String... col) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.orderByAsc(col);
        }
        return this;
    }

    public QueryBuilder in(String col, Object... val) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.in(col, val);
        }
        return this;
    }

    public QueryBuilder notIn(String col, Object... val) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.notIn(col, val);
        }
        return this;
    }

    public QueryBuilder between(String col, Object val, Object val2) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.between(col, val, val2);
        }
        return this;
    }

    public QueryBuilder orderByDesc(String... col) {
        if (!StringUtils.isEmpty(col)) {
            queryWrapper.orderByDesc(col);
        }
        return this;
    }

    public QueryBuilder select(String... col) {
        queryWrapper.select(col);
        return this;
    }

    public QueryWrapper buildCount() {
        return this.queryWrapper;
    }

    public QueryWrapper build() {
        return this.queryWrapper;
    }
}
