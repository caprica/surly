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

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Specification for a Spring Data repository providing persistence for {@link UrlInfo} instances.
 * <p>
 * <em>Developer Note:</em>
 * <p>
 * The Spring Data repository gives us automatic implementations for various CRUD operations.
 * <p>
 * In this case, we also need a custom query which is automatically generated based on the method name.
 */
@EnableScan
public interface UrlInfoRepository extends CrudRepository<UrlInfo, String> {

    /**
     * Find an entity for a particular long URL value.
     *
     * @param longUrl long URL value
     * @return optional entity
     */
    Optional<UrlInfo> findByLongUrl(String longUrl);
}
