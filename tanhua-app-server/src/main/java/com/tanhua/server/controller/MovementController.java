package com.tanhua.server.controller;

import com.tanhua.mongo.Movement;
import com.tanhua.server.service.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@RestController
@RequestMapping("/movements")
public class MovementController {
    @Autowired
    private MovementService movementService;

    public ResponseEntity<Object> publishMovement(Movement movement, MultipartFile[] files) {

        return ResponseEntity.ok("");
    }


}
