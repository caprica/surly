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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.net.URL;

import static org.slf4j.LoggerFactory.getLogger;

// FIXME refactor this to throw exceptions and use an exceptionhandler rather than returning responseentity?

/**
 * Web-service end-point for the URL Shortener services.
 */
@Api(
    tags = {"URL Shortener"}
)
@RestController
public class UrlShortenerController {

    private static final Logger log = getLogger(UrlShortenerController.class);

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
    @ApiOperation(value = "Create, or get an already existing, short URL token for a given long URL")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "The short URL token"),
        @ApiResponse(code = 400, message = "Invalid URL supplied"),
    })
    @PostMapping(value = "/u", consumes = "text/plain", produces = "text/plain")
    public ResponseEntity<String> getShortUrl(
        @ApiParam(name = "longUrl", value = "Ordinary URL", example = "httpp://www.google.com") @RequestBody String longUrl) {
        log.info("getShortUrl(longUrl={})", longUrl);
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
    @ApiOperation(value = "Get a long URL for a given short URL token")
    @ApiResponses(value = {
        @ApiResponse(code = 302, message = "The short URL token"), // FIXME 200 would do here really
        @ApiResponse(code = 404, message = "Long URL not found for the given short URL token"),
    })
    @GetMapping(value = "/u/{token}", produces = "text/plain")
    public ResponseEntity<String> getLongUrl(
        @ApiParam(name = "token", value = "Short URL token", example = "14NLK7") @PathVariable("token") String token) {
        log.info("getLongUrl(token={})", token);
        return urlShortenerService.getLongUrl(token)
            .map(longUrl -> new ResponseEntity<>(longUrl, HttpStatus.FOUND))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
