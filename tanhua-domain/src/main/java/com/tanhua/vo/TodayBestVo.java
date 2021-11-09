package com.tanhua.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/6
 */
@Data
public class TodayBestVo implements Serializable {
    private Long id;
    private String avatar;
    private String nickname;
    private String gender;
    private Integer age;
    private String[] tags;
    private Integer fateValue;
    }
