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

package uk.co.caprica.surly.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.caprica.surly.service.UrlShortenerService;

import java.net.MalformedURLException;
import java.net.URL;

// FIXME refactor this to throw exceptions and use an exceptionhandler rather than returning responseentity?

/**
 * Web-service end-point for the URL Shortener services.
 */
@RestController
public class UrlShortenerController {

    private final UrlShortenerService urlShortenerService;

    public UrlShortenerController(UrlShortenerService urlShortenerService) {
        this.urlShortenerService = urlShortenerService;
    }

    /**
     * Get, or create, a short URL for a given long URL.
     *
     * @param longUrl long URL
     * @return short URL token
     */
    @PostMapping("/u")
    public ResponseEntity<String> getShortUrl(@RequestBody String longUrl) {
        try {
            new URL(longUrl);
            return new ResponseEntity<>(urlShortenerService.getShortUrl(longUrl), HttpStatus.CREATED);
        } catch (MalformedURLException e) {
            return new ResponseEntity<>(String.format("Invalid URL: %s", e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get a long URL for a given short URL token.
     *
     * @param token short URL token
     * @return long URL, if it exists, otherwise a not-found response
     */
    @GetMapping("/u/{token}")
    public ResponseEntity<String> getLongUrl(@PathVariable("token") String token) {
        return urlShortenerService.getLongUrl(token)
            .map(longUrl -> new ResponseEntity<>(longUrl, HttpStatus.FOUND))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
