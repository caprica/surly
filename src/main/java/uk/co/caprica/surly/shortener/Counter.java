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

package uk.co.caprica.surly.shortener;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

/**
 * Domain object representing a simple counter.
 * <p>
 * <em>Developer Note:</em>
 * <p>
 * Ordinarily immutable value objects would be preferred, but the standard JavaBean pattern is adopted here for use with Spring Data.
 */
@DynamoDBTable(tableName="Counter")
public class Counter {

    @DynamoDBHashKey
    private String name;

    @DynamoDBAttribute
    private long value;

    public Counter() {
    }

    public Counter(String name, long value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!o.getClass().equals(getClass())) {
            return false;
        }
        Counter other = (Counter) o;
        return
            equal(name, other.name) &&
            equal(value, other.value);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("name", name)
            .add("value", value)
            .toString();
    }
}
