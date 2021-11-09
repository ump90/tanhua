package com.tanhua.mongo;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author UMP90
 * @date 2021/11/7
 */
@Data
@Document(collection = "movement_timeLine")
public class MovementTimeLine implements Serializable {
    private static final long serialVersionUID = -8692940559827355541L;
    private ObjectId id;
    private ObjectId movementId;
    private Long userId;
    private Long friendId;
    private Long created;
}
