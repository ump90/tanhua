package com.tanhua.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tanhua.pojo.Log;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author UMP90
 * @date 2021/11/23
 */
@Mapper
public interface LogMapper extends BaseMapper<Log> {
  @Select(
      "SELECT COUNT(DISTINCT user_id) FROM tb_log where tb_log.log_time=#{date} and type not in ('0101','0102');")
  Long countActiveUser(String date);

  Long countRetentionUser(String today, String yesterday);
}
