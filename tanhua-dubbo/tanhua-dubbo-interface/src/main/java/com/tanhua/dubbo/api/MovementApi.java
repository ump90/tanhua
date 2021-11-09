package com.tanhua.dubbo.api;

import com.tanhua.mongo.Movement;

/**
 * @author UMP90
 * @date 2021/11/7
 */

public interface MovementApi {
    void publishMovement(Movement movement);

}
