package com.tanhua.server.exception;

import com.tanhua.pojo.ErrorResult;
import lombok.Data;

/**
 * @author UMP90
 * @date 2021/11/3
 */
@Data
public class BusinessException extends RuntimeException{
    public ErrorResult errorResult;
    public BusinessException(ErrorResult errorResult){
        super(errorResult.getErrMessage());
        this.errorResult=errorResult;
    }
}
