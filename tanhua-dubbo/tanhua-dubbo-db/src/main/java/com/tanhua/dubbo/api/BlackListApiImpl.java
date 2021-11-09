package com.tanhua.dubbo.api;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tanhua.dubbo.mapper.BlackListMapper;
import com.tanhua.pojo.BlackList;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@DubboService
public class BlackListApiImpl extends ServiceImpl<BlackListMapper, BlackList> implements BlackListApi{


    @Override
    public void deleteByBlackUserId(Long userId,Long blackUserId) {
        LambdaQueryWrapper<BlackList> listLambdaQueryWrapper=new LambdaQueryWrapper<>();
        listLambdaQueryWrapper.eq(BlackList::getBlackUserId, blackUserId);
        listLambdaQueryWrapper.eq(BlackList::getUserId, userId);
        this.remove(listLambdaQueryWrapper);
    }

    @Override
    public List<BlackList> getBlackListByUserId(Long userId) {
        LambdaQueryWrapper<BlackList> listLambdaQueryWrapper=new LambdaQueryWrapper<>();
        listLambdaQueryWrapper.eq(BlackList::getUserId, userId);
        return this.list(listLambdaQueryWrapper);
    }
}
