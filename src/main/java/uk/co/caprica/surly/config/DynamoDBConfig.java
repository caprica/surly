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
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.caprica.surly.shortener.Counter;
import uk.co.caprica.surly.shortener.UrlInfo;

/**
 * Configuration for DynamoDB.
 */
@Configuration
@EnableDynamoDBRepositories(basePackages = "uk.co.caprica.surly.shortener")
public class DynamoDBConfig {

    @Value("${amazon.dynamodb.endpoint}")
    private String amazonDynamoDBEndpoint;

    @Value("${amazon.aws.region}")
    private String amazonAWSRegion;

    @Value("${amazon.aws.accesskey}")
    private String amazonAWSAccessKey;

    @Value("${amazon.aws.secretkey}")
    private String amazonAWSSecretKey;

    @Bean
    public AmazonDynamoDB amazonDynamoDB() {
        AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(amazonDynamoDBEndpoint, amazonAWSRegion))
            .withCredentials(new AWSStaticCredentialsProvider(amazonAWSCredentials()))
            .build();

        DynamoDBMapper dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);

        deleteTables(amazonDynamoDB, dynamoDBMapper);
        createTables(amazonDynamoDB, dynamoDBMapper);

        return amazonDynamoDB;
    }

    // FIXME this needs @Bean?
    @Bean
    public AWSCredentials amazonAWSCredentials() {
        return new BasicAWSCredentials(amazonAWSAccessKey, amazonAWSSecretKey);
    }

    private static void deleteTables(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper) {
        try {
            DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(UrlInfo.class);
            DeleteTableResult deleteTableResult = amazonDynamoDB.deleteTable(deleteTableRequest);
        } catch (ResourceNotFoundException e) {
        }

        try {
            DeleteTableRequest deleteTableRequest = dynamoDBMapper.generateDeleteTableRequest(Counter.class);
            DeleteTableResult deleteTableResult = amazonDynamoDB.deleteTable(deleteTableRequest);
        } catch (ResourceNotFoundException e) {
        }
    }

    private static void createTables(AmazonDynamoDB amazonDynamoDB, DynamoDBMapper dynamoDBMapper) {
        CreateTableRequest createUrlInfoTableRequest = dynamoDBMapper.generateCreateTableRequest(UrlInfo.class);
        createUrlInfoTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        CreateTableResult createUrlInfoTableResponse = amazonDynamoDB.createTable(createUrlInfoTableRequest);

        CreateTableRequest createCounterTableRequest = dynamoDBMapper.generateCreateTableRequest(Counter.class);
        createCounterTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        CreateTableResult createCounterTableResponse = amazonDynamoDB.createTable(createCounterTableRequest);
    }
}