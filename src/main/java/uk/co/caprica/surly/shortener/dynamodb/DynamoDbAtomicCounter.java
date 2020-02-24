/*
 * Copyright 2020 Caprica Software Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.co.caprica.surly.shortener.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeAction;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.AttributeValueUpdate;
import com.amazonaws.services.dynamodbv2.model.ReturnValue;
import com.amazonaws.services.dynamodbv2.model.UpdateItemRequest;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import uk.co.caprica.surly.shortener.AtomicCounter;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * An atomic counter implemented using DynamoDB.
 */
@Component
public class DynamoDbAtomicCounter implements AtomicCounter {

    private static final Logger log = getLogger(DynamoDbAtomicCounter.class);

    private static final String COUNTER_NAME = "HashId";

    private final AmazonDynamoDB amazonDynamoDb;

    public DynamoDbAtomicCounter(AmazonDynamoDB amazonDynamoDb) {
        this.amazonDynamoDb = amazonDynamoDb;
    }

    @Override
    public long nextValue() {
        log.debug("nextValue()");
        UpdateItemRequest updateItemRequest =
            new UpdateItemRequest()
                .withTableName("Counter")
                .addKeyEntry("name", new AttributeValue()
                    .withS(COUNTER_NAME)
                )
                .addAttributeUpdatesEntry("value", new AttributeValueUpdate()
                    .withValue(new AttributeValue().withN("1"))
                    .withAction(AttributeAction.ADD)
                )
                .withReturnValues(ReturnValue.UPDATED_NEW);

        return Long.parseLong(amazonDynamoDb.updateItem(updateItemRequest).getAttributes().get("value").getN());
    }
}
