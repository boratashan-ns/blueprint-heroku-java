package com.blueprints.heroku.views.helloworld;

import com.blueprints.heroku.commons.InvalidCredentialsException;
import com.blueprints.heroku.commons.NewstoreRestClient;
import com.blueprints.heroku.commons.RestClientException;
import com.blueprints.heroku.data.DataClient;
import com.blueprints.heroku.eventstream.EventStreamService;
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



@PageTitle("Hello World")
@Route(value = "hello", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class HelloWorldView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    @Autowired
    private NewstoreRestClient restClient;
    @Autowired
    private EventStreamService eventStreamService;

    @Autowired
    public HelloWorldView(DataClient dataClient) {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            try {
                //String res = dataClient.getAppInfo();

                String res = getIntegrationDetails();
                Notification.show("Hello " + name.getValue() + " appinfo->" + res);
            }catch (Exception exception) {
                Notification.show("Exception : "+exception.getMessage());
            }
        });

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello);
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
            kong.unirest.HttpResponse<String> response = restClient.get("/api/v1/org/integrations/eventstream/", null, null);
            message = response.getBody().toString();
        } catch (InvalidCredentialsException e) {
            message = e.getMessage();
        } catch (RestClientException e) {
            message = e.getMessage();
        }
        return message;
    }

}
