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
@Document(collection = "sequence")
public class Sequence implements Serializable {
    private static final long serialVersionUID = -2736590679556870076L;
    private ObjectId objectId;
    private Long seqId;
    private String collName;
}
