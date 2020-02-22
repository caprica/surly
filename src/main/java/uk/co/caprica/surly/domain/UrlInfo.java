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

package uk.co.caprica.surly.domain;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Date;

import static com.google.common.base.MoreObjects.toStringHelper;
import static com.google.common.base.Objects.equal;

/**
 * Domain object representing information about a shortened URL.
 * <p>
 * <em>Developer Note:</em>
 * <p>
 * Ordinarily immutable value objects would be preferred, but the standard JavaBean pattern is adopted here for use with Spring Data.
 */
@DynamoDBTable(tableName="UrlInfo")
final public class UrlInfo {

    @DynamoDBHashKey
    private String token;

    @DynamoDBAttribute
    private String longUrl;

    @DynamoDBAttribute
    private Date created;

    public UrlInfo() {
    }

    public UrlInfo(String token, String longUrl, Date created) {
        this.token = token;
        this.longUrl = longUrl;
        this.created = created;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLongUrl() {
        return longUrl;
    }

    public void setLongUrl(String longUrl) {
        this.longUrl = longUrl;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        return token.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (!o.getClass().equals(getClass())) {
            return false;
        }
        UrlInfo other = (UrlInfo) o;
        return
            equal(token, other.token) &&
            equal(longUrl, other.longUrl) &&
            equal(created, other.created);
    }

    @Override
    public String toString() {
        return toStringHelper(this)
            .add("token", token)
            .add("longUrl", longUrl)
            .add("created", created)
            .toString();
    }
}
