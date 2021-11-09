package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.QuestionMapper;
import com.tanhua.pojo.Question;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@DubboService
public class QuestionApiImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionApi{
}
