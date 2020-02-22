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

package uk.co.caprica.surly;

import java.util.Optional;

/**
 * Specification for the URL Shortener microservice.
 * <p>
 * <em>Developer Note:</em>
 * <p>
 * Using the interfaces this way means we do not need to use ugly DefaultXyzService or XyzServiceImpl naming conventions.
 * <p>
 * Any other interfaces used for the component would also be specified in here.
 */
public interface UrlShortener {

    /**
     * Service specification.
     */
    interface Service {

        /**
         * Get (or create) a short URL for a given long URL.
         *
         * @param longUrl long URL
         * @return short URL token
         */
        String getShortUrl(String longUrl);

        /**
         * Get an existing long URL for a given short URL token.
         *
         * @param token short URL token
         * @return optional long URL
         */
        Optional<String> getLongUrl(String token);
    }
}
