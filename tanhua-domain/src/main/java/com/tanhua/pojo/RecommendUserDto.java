package com.tanhua.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Data

public class RecommendUserDto implements Serializable {
    private Integer page;

    private Integer pagesize;
    private String gender;
    private String lastLogin;
    private Integer age;
    private String city;
    private String education;
}
