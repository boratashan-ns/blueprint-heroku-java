package com.blueprints.heroku.eventstream;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.http.HttpResponse;


@RestController
@RequestMapping("/eventstream")
public class EventStreamWebHook {
    public static final String EVENT_STREAM_LISTENER_PATH = "/eventstream/event";

    private Logger logger = LoggerFactory.getLogger(EventStreamWebHook.class);

    @PostMapping("/event")
    public ResponseEntity<String> webhookListener(@RequestBody String payload) {
        logger.info(String.format("[%s], [%s]", "event-stream recv", payload ));
        System.out.println(String.format("[%s], [%s]", "event-stream recv", payload ));
        return ResponseEntity.ok("");
    }

}
