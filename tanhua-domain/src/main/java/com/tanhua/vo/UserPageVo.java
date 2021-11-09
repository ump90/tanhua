package com.tanhua.vo;

import com.tanhua.pojo.BlackList;
import com.tanhua.pojo.User;
import com.tanhua.pojo.UserInfo;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@Data
public class UserPageVo implements Serializable {
    private int counts;
    private int pagesize;
    private int pages;
    private int page;
    private List<UserInfo> items;

}
