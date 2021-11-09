package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.SettingMapper;
import com.tanhua.pojo.Setting;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@DubboService
public class SettingApiImpl extends ServiceImpl<SettingMapper, Setting> implements SettingApi {
}
