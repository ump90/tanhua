package com.tanhua.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/4
 */
@Data
public class SettingVo implements Serializable {
    private Long id;
    private String strangerQuestion="";
    private String phone;
    private boolean likeNotification=true;
    private boolean pinglunNotification=true;
    private boolean gonggaoNotification=true;
}
