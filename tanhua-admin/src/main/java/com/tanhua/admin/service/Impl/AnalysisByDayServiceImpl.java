package com.tanhua.admin.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.admin.mapper.AnalysisByDayMapper;
import com.tanhua.admin.service.AnalysisByDayService;
import com.tanhua.pojo.AnalysisByDay;
import org.springframework.stereotype.Service;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@Service
public class AnalysisByDayServiceImpl extends ServiceImpl<AnalysisByDayMapper, AnalysisByDay>
    implements AnalysisByDayService {}
