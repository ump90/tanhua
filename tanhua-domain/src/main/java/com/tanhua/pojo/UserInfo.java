package com.tanhua.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户信息表
 * @author UMP90
 * @TableName tb_user_info
 */
@TableName(value ="tb_user_info")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo extends BasePojo implements Serializable{
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户标签：多个用逗号分隔
     */
    private String tags;

    /**
     * 性别，1-男，2-女，3-未知
     */
    private String gender;

    /**
     * 用户年龄
     */
    private Integer age;

    /**
     * 学历
     */
    private String education;

    /**
     * 居住城市
     */
    private String city;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 封面图片
     */
    private String coverPic;

    /**
     * 行业
     */
    private String profession;

    /**
     * 收入
     */
    private String income;



    /**
     * 0：未婚，1：已婚
     */
    private Integer marriage;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}