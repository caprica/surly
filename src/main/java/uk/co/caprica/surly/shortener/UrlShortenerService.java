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

import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * Implementation of a URL Shortener service.
 */
@Service
public class UrlShortenerService implements UrlShortener.Service {

    private final UrlInfoRepository urlInfoRepository;

    private final HashGenerator hashGenerator;

    private final AtomicCounter atomicCounter;

    public UrlShortenerService(UrlInfoRepository urlInfoRepository, AtomicCounter atomicCounter, HashGenerator hashGenerator) {
        this.urlInfoRepository = urlInfoRepository;
        this.atomicCounter = atomicCounter;
        this.hashGenerator = hashGenerator;
    }

    @Override
    public String getShortUrl(String longUrl) {
        return urlInfoRepository.findByLongUrl(longUrl)
            .map(UrlInfo::getToken)
            .orElseGet(() -> createShortUrl(longUrl));
    }

    @Override
    public Optional<String> getLongUrl(String token) {
        return urlInfoRepository.findById(token)
            .map(UrlInfo::getLongUrl);
    }

    /**
     * Create a new short URL for the given long URL.
     * <p>
     * There is the possibility here to end up with two different hashes for the same URL if the requests are submitted at exactly the same time - such a
     * circumstance would be extremely likely, but even if it did occur it does not really matter.
     *
     * @param longUrl long URL to convert
     * @return short URL
     */
    private String createShortUrl(String longUrl) {
        String token = hashGenerator.generateHash(atomicCounter.nextValue());
        urlInfoRepository.save(new UrlInfo(token, longUrl, new Date()));    // FIXME we might consider implementing a clock component instead of new Date() here
        return token;
    }
}
