package com.blueprints.heroku.views;

import com.blueprints.heroku.commons.InvalidCredentialsException;
import com.blueprints.heroku.commons.NewstoreRestClient;
import com.blueprints.heroku.commons.RestClientException;
import com.blueprints.heroku.queue.AmqpService;
import com.blueprints.heroku.services.eventstream.EventStreamApiClient;
import com.blueprints.heroku.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;


@PageTitle("Event stream")
@Route(value = "eventstream", layout = MainLayout.class)
public class EventStreamView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    @Autowired
    private NewstoreRestClient restClient;
    @Autowired
    private EventStreamApiClient eventStreamService;

    @Autowired
    private AmqpService amqpService;


    public EventStreamView() {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            try {
                //String res = dataClient.getAppInfo();
                amqpService.send("orders", "Sending a message");
                String res = getIntegrationDetails();
                Notification.show("Hello " + name.getValue() + " appinfo->" + res);
            }catch (Exception exception) {
                Notification.show("Exception : "+exception.getMessage());
            }
        });

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

       // add(name, sayHello);
    }

    public String getIntegrationDetails() {
        String message;
        try {
            HttpResponse<String> response = eventStreamService.getIntegrationDetails("test");
            message = response.getBody().toString();
        } catch (InvalidCredentialsException e) {
            message = e.getMessage();
        } catch (RestClientException e) {
            message = e.getMessage();
        }
        return message;
    }


    public String getEventStreamRegistrations () {
        String message;
        try {
            HttpResponse<String> response = restClient.get("/api/v1/org/integrations/eventstream/", null, null);
            message = response.getBody().toString();
        } catch (InvalidCredentialsException e) {
            message = e.getMessage();
        } catch (RestClientException e) {
            message = e.getMessage();
        }
        return message;
    }

}
