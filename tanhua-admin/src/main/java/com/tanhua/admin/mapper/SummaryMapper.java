package com.tanhua.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author UMP90
 * @date 2021/11/24
 */
@Mapper
public interface SummaryMapper {
  @Select("select count(distinct tb_user.id) from tb_user;")
  Long countUser();

  @Select(
      "select count(distinct user_id) from tb_log where log_time between #{startTime} and #{endTime} and type not in ('0101','0102');")
  Long countActiveUser(String startTime, String endTime);

  @Select("select count(distinct user_id) from tb_log where type='0102' and log_time=#{time};")
  Long countNewUser(String time);

  @Select("select count(*) from tb_log where log_time=#{time} and type='0101'")
  Long countLoginTime(String time);
}
