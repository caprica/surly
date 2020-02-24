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

package uk.co.caprica.surly.shortener.hashids;

import org.hashids.Hashids;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import uk.co.caprica.surly.shortener.HashGenerator;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Implementation of a component that generates hash strings using the Hashids library.
 */
@Component
public class HashidsHashGenerator implements HashGenerator {

    private static final Logger log = getLogger(HashidsHashGenerator.class);

    private static final Hashids hashids = new Hashids("i am a secret salt", 6); // FIXME pull from environment config?

    @Override
    public String generateHash(long value) {
        log.debug("generateHash(value={})", value);
        return hashids.encode(value);
    }
}
