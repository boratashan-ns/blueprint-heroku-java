package com.blueprints.heroku.api;


import com.blueprints.heroku.services.eventstream.EventStreamProcessor;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.time.*;
import java.util.UUID;


@RestController
@RequestMapping("/eventstream")
public class EventStreamWebHook {
    public static final String EVENT_STREAM_LISTENER_PATH = "/eventstream/event";

    private Logger logger = LoggerFactory.getLogger(EventStreamWebHook.class);


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EventStreamProcessor eventStreamProcessor;

    @PostMapping("/event")
    public ResponseEntity<String> webhookListener(@RequestBody String payload) {
        logger.debug(String.format("[%s], [%s]", "event-stream recv", payload ));
        eventStreamProcessor.processEventStreamEvent(payload);
        return ResponseEntity.ok("");
    }
}
