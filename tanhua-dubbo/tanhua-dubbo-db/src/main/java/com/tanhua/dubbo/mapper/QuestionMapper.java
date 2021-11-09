package com.tanhua.dubbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.pojo.Question;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}
