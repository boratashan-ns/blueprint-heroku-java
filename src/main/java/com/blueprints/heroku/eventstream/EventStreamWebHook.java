package com.blueprints.heroku.eventstream;


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
import java.time.format.DateTimeFormatter;
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
        logger.info(String.format("[%s], [%s]", "event-stream recv", payload ));
        System.out.println(String.format("[%s], [%s]", "event-stream recv", payload ));
        eventStreamProcessor.processEventStreamEvent(payload);
        //savePayloadToDb(payload);
        return ResponseEntity.ok("");
    }




    private void savePayloadToDb(String payload) {
        try {
            logger.info("Start inserting payload");
            logger.info(String.format("Payload -> %s", payload));
            String DML = "INSERT INTO public.event_stream_payloads(\n" +
                    "\ttenant, name, published_at, event_id, event_tag, payload, event_seq_nr, is_processed)\n" +
                    "\tVALUES (?, ?, ?, ?, ?, ?::json, ?, ?);";

            JsonElement jsonElement = JsonParser.parseString(payload);
            String tenant = jsonElement.getAsJsonObject().get("tenant").getAsString();
            String name = jsonElement.getAsJsonObject().get("name").getAsString();
            String timestamp = jsonElement.getAsJsonObject().get("published_at").getAsString();
            Timestamp published_at = Timestamp.from(Instant.parse(timestamp));

            String id = jsonElement.getAsJsonObject().get("payload").getAsJsonObject().get("id").getAsString();
            UUID eventId = UUID.fromString(id);
            String pload = jsonElement.getAsJsonObject().get("payload").toString();
            jdbcTemplate.update(DML, new Object[]{
                    tenant,
                    name,
                    published_at,
                    eventId,
                    "",
                    pload,
                    1,
                    false
            });
        } catch (DuplicateKeyException e) {
            logger.info(String.format("Exception while inserting eventstream event payload, exception -> %s", e.toString()));
            logger.info("Skip duplicate record");
        } catch (Exception e){
            logger.info(String.format("Exception while inserting eventstream event payload, exception -> %s", e.toString()));
            throw e;
        }
    }

}
