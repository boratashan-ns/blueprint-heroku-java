package com.blueprints.heroku.eventstream;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;


import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

@Component
public class EventStreamProcessor {
    public static final String TOPIC_ORDER_PROCESS = "orders.req.process";

    private Logger logger = LoggerFactory.getLogger(EventStreamProcessor.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AmqpService amqpService;

    public void processEventStreamEvent(String payload) {
        Event event = Event.parse(payload);

        switch (EventEntity.from(event.getName())) {
            case ORDER:
                UUID uid = this.saveEventToDb(event);
                if (event.getName().equalsIgnoreCase("order.completed")) {
                    SimpleQueueMessage msg = new SimpleQueueMessage("order.completed.request", uid.toString());
                    amqpService.send(TOPIC_ORDER_PROCESS, msg.convertToJsonString());
                }
                break;
            case INVENTORY:
                this.saveEventToDb(event, true);
                break;
            case UNKNOWN:
                logger.warn(String.format("[%s]-[Unknown event has been received], [Event]-[%s] [Payload]-[%s] ", "ERR_EVENTSTREAM_UNKNOWN", event.toString(), payload));
                break;
        }
    }



    @RabbitListener(queues = TOPIC_ORDER_PROCESS)
    public void processMessage(String content) {
        logger.info(String.format("Received -> %s", content));
        SimpleQueueMessage message =  SimpleQueueMessage.parse(content);
        String rowId = message.getMessage();
        this.createOrderData(UUID.fromString(rowId));

    }

    public UUID saveEventToDb(Event event) {
        return this.saveEventToDb(event, false);
    }

    public UUID saveEventToDb(Event event, boolean markIsProcessed) {
        logger.debug("Start inserting payload");
        logger.debug(String.format("Payload -> %s", event.toString()));
        String DML = "INSERT INTO public.event_stream_payloads(\n" +
                "\tuid,tenant, name, published_at, event_id, event_tag, payload, event_seq_nr, is_processed)\n" +
                "\tVALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try {

            String id = event.getPayload().getAsJsonObject().get("id").getAsString();
            UUID eventId = UUID.fromString(id);
            KeyHolder keyHolder = new GeneratedKeyHolder();
            final UUID uid = UUID.randomUUID();
            int r = jdbcTemplate.update(connection -> {

                PreparedStatement ps = connection.prepareStatement(DML);
                ps.setObject(1, uid);
                ps.setString(2, event.getTenant());
                ps.setString(3, event.getName());
                ps.setTimestamp(4, event.getPublishedAt());
                ps.setObject(5, eventId);
                ps.setString(6, eventId.toString());
                ps.setString(7, event.getPayload().toString());
                ps.setInt(8, 1);
                ps.setBoolean(9, markIsProcessed?true:false);
                return ps;
            }, keyHolder);
            return uid;
        } catch (DuplicateKeyException e) {
            logger.info(String.format("Exception while inserting eventstream event payload, exception -> %s", e.toString()));
            logger.info("Skip duplicate record");
            throw e;
        } catch (Exception e) {
            logger.info(String.format("Exception while inserting eventstream event payload, exception -> %s", e.toString()));
            throw e;
        }
    }


    private void createOrderData(UUID payloadId) {

        String qry = "SELECT * FROM public.event_stream_payloads AS a\n" +
                "INNER JOIN public.event_stream_payloads as b on (a.event_id = b.event_id) and (b.uid = ? )";

        List<ImmutablePair<String, String>> eventNames = new ArrayList<>();
        String orderId = "";
        String externalId = "";

        jdbcTemplate.query(qry, new Object[]{payloadId}, new int[] {Types.VARCHAR}, rs -> {
            String eventName = "";
            while (rs.next()) {
                eventNames.add(ImmutablePair.of(rs.getString("name"), rs.getString("payload")));
                eventName = eventNames.add(rs.getString("name"));
                orderId = rs.getString("event_id");
                String payload = rs.getString("payload");
                if (eventName.equalsIgnoreCase("order.opened") || eventName.equalsIgnoreCase("order.created")) {
                    JsonObject jsonObject =  JsonParser.parseString(payload).getAsJsonObject();
                    if (jsonObject.has("external_id")) {
                        externalId = jsonObject.getAsJsonObject("external_id").getAsString();
                    }
                }
            }
        });
        StringBuilder events = new StringBuilder();
        events.append("[");
        boolean added = false;
        for(ImmutablePair<String, String> psir : eventNames) {
            String payload = psir.getRight();
            events.append(payload).append(",");
            added = true;
        }
        if (added) {
            events.deleteCharAt(events.length() - 1);
        }
        events.append("]");
        String insertQry = "INSERT INTO public.orders (orderid, order_ext_id, status, events) VALUES (?, ?, ?, ?) ";
        jdbcTemplate.update(insertQry,
                            new Object[] {orderId, externalId, "", events.toString()},
                            new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR});

        //UUID event_id = jdbcTemplate.queryForObject(qryReadFromPayload, new Object[] {payloadId}, UUID.class);

    }

}
