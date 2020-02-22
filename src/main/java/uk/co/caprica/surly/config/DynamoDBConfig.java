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

package uk.co.caprica.surly.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import uk.co.caprica.surly.domain.Counter;
import uk.co.caprica.surly.domain.UrlInfo;

/**
 * Configuration for DynamoDB.
 */
@Configuration
@EnableDynamoDBRepositories(basePackages = "uk.co.caprica.surly.repository")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = new AmazonDynamoDBClient(amazonAWSCredentials());

        if (!StringUtils.isEmpty(amazonDynamoDBEndpoint)) {
            amazonDynamoDB.setEndpoint(amazonDynamoDBEndpoint);
        }

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        try {
            DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(UrlInfo.class);
            amazonDynamoDB.deleteTable(deleteTableRequest);
        } catch (ResourceNotFoundException e) {
        }

        try {
            DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(Counter.class);
            amazonDynamoDB.deleteTable(deleteTableRequest);
        } catch (ResourceNotFoundException e) {
        }

        CreateTableRequest tableRequest = dynamoDBMapper.generateCreateTableRequest(UrlInfo.class);
        tableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        CreateTableResult r = amazonDynamoDB.createTable(tableRequest);
        System.out.println("REATE TABLE RESULT " + r);

        CreateTableRequest t2 = dynamoDBMapper.generateCreateTableRequest(Counter.class);
        t2.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        r = amazonDynamoDB.createTable(t2);
        System.out.println("REATE TABLE RESULT " + r);


        DescribeTableRequest dt = new DescribeTableRequest().withTableName("UrlInfo");
        DescribeTableResult dr = amazonDynamoDB.describeTable(dt);
        System.out.println("DESCRIBE TABLE RESULT " + dr);

//        counterRepository.save(new Counter("HashId", 0L));

        return amazonDynamoDB;
    }

    // FIXME this needs @Bean?
    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }
}