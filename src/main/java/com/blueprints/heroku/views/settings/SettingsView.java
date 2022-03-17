package com.blueprints.heroku.views.settings;

import com.blueprints.heroku.commons.InvalidCredentialsException;
import com.blueprints.heroku.commons.RestClientException;
import com.blueprints.heroku.data.DataClient;
import com.blueprints.heroku.eventstream.EventStreamService;
import com.blueprints.heroku.views.MainLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;


@PageTitle("Settings")
@Route(value = "settings", layout = MainLayout.class)
public class SettingsView extends VerticalLayout {


    private String esIntegrationId;
    private String esDefaultCallbackUrl;
    private String esDefaultCallbackApiKey;


    private EventStreamService eventStreamService;
    private DataClient dataClient;


    private Button enableEventStreamListener;
    private Button disableEventStreamListener;
    private Button registerEventStreamListener;
    private TextArea textArea;

    private boolean eventStreamEnabledDisabledState;






    @Autowired
    public SettingsView(DataClient dataClient, EventStreamService eventStreamService,
                        @Value("${appinfo.eventstream.integrationid}") String esIntegrationId,
                        @Value("${appinfo.eventstream.callbackurl}") String esDefaultCallbackUrl,
                        @Value("${appinfo.eventstream.apikey}") String esDefaultCallbackApiKey) {
        this.dataClient = dataClient;
        this.eventStreamService = eventStreamService;
        this.esIntegrationId = esIntegrationId;
        this.esDefaultCallbackUrl = esDefaultCallbackUrl;
        this.esDefaultCallbackApiKey = esDefaultCallbackApiKey;
        buildUI(this);

    }

    private boolean getEventStreamEnableDisableState() {
        return false;
    }

    private SettingsView setEventStreamEnableDisableState(boolean isEnable) {
        try {
            HttpResponse<String> response;
            if (isEnable) {
                response = eventStreamService.startTheIntegration(this.esIntegrationId);
            } else {
                response = eventStreamService.stopTheIntegration(this.esIntegrationId);
            }
            if (response.isSuccess()) {
                eventStreamEnabledDisabledState = isEnable;
                enableEventStreamListener.setEnabled(!isEnable);
                disableEventStreamListener.setEnabled(isEnable);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Http Code : %d, Http Status  :%s \n", response.getStatus(), response.getStatusText()));
                sb.append(response.getBody().toString());
                Notification.show(sb.toString());
                addToScreenLog(sb.toString());
            }
        } catch (InvalidCredentialsException e) {
            Notification.show(e.getMessage());
            addToScreenLog(e.getMessage());
        } catch (RestClientException e) {
            Notification.show(e.getMessage());
            addToScreenLog(e.getMessage());
        }
        return this;
    }

    private void buildUI(SettingsView owner) {
        this.buildEventStreamStatusCheckSection(owner);
        this.buildEventStreamRegistrationSection(owner);
        this.buildEventStartStopSection(owner);
        this.buildLogSection(owner);
    }

    private void buildEventStreamRegistrationSection(SettingsView owner) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setMargin(true);
        registerEventStreamListener = new Button("Register", e -> {
            try {
                HttpResponse<String> response = eventStreamService.registerNewIntegration(esIntegrationId, esDefaultCallbackUrl, esDefaultCallbackApiKey);
                StringBuilder sb = new StringBuilder();
                sb.append(String.format("Http Code : %d, Http Status  :%s \n", response.getStatus(), response.getStatusText()));
                sb.append(response.getBody().toString());
                //Notification.show(sb.toString());
                addToScreenLog(sb.toString());
                checkIntegrationStatus();
                /*if (response.isSuccess()) {
                    registerEventStreamListener.setEnabled(false);
                }*/
            } catch (InvalidCredentialsException ex) {
                Notification.show(ex.getMessage());

            } catch (RestClientException ex) {
                Notification.show(ex.getMessage());
            }
        });
        registerEventStreamListener.setEnabled(false);
//        setEventStreamEnableDisableState(eventStreamEnabledDisabledState);
        Span text = new Span("Event stream registration");
        layout.setVerticalComponentAlignment(Alignment.CENTER, text, registerEventStreamListener);
        layout.add(text, registerEventStreamListener);
        owner.add(layout);
    }

    private void buildEventStartStopSection(SettingsView owner) {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setMargin(true);
        enableEventStreamListener = new Button("Enable", e -> {
            this.setEventStreamEnableDisableState(true);
        });
        disableEventStreamListener = new Button("Disable", e -> {
            this.setEventStreamEnableDisableState(false);
        });
        enableEventStreamListener.setEnabled(false);
        disableEventStreamListener.setEnabled(false);
        //setEventStreamEnableDisableState(eventStreamEnabledDisabledState);
        Span text = new Span("Event stream");
        layout.setVerticalComponentAlignment(Alignment.CENTER, text, enableEventStreamListener, disableEventStreamListener);
        layout.add(text, enableEventStreamListener, disableEventStreamListener);
        owner.add(layout);
    }

    private void buildLogSection(SettingsView owner) {
        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        layout.setWidthFull();
        layout.setHeightFull();
        textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setLabel("Log");
        textArea.setValue("");
        layout.add(textArea);
        owner.add(layout);
    }

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);
        //  checkIntegrationStatus();
    }

    private void buildEventStreamStatusCheckSection(SettingsView owner) {

        HorizontalLayout layoutResponse = new HorizontalLayout();
        layoutResponse.setWidthFull();


        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setMargin(true);

        Span text = new Span("Check event-stream client status");
        Button statusCheck = new Button("Check", e -> {
                checkIntegrationStatus();
        });

        layout.setVerticalComponentAlignment(Alignment.CENTER, text, statusCheck);
        layout.add(text, statusCheck);
        owner.add(layout);
        owner.add(layoutResponse);


    }


    private void checkIntegrationStatus() {
        boolean isRegistered = true;
        StringBuilder sb = new StringBuilder();
        try {
            HttpResponse<String> response = eventStreamService.getIntegrationDetails(this.esIntegrationId);
            if (response.isSuccess()) {
                isRegistered = true;
                JsonElement jsonElement = JsonParser.parseString(response.getBody().toString());
                String status = jsonElement.getAsJsonObject().get("status").getAsString();
                //promptError(status);



                if (status.equalsIgnoreCase("stopped")) {
                    enableEventStreamListener.setEnabled(true);
                    disableEventStreamListener.setEnabled(false);
                }
                if (status.equalsIgnoreCase("running")) {
                    enableEventStreamListener.setEnabled(false);
                    disableEventStreamListener.setEnabled(true);
                }

            } else {
                enableEventStreamListener.setEnabled(false);
                disableEventStreamListener.setEnabled(false);

                switch (response.getStatus()) {
                    case 404: {
                        isRegistered = false;
                        promptError("Integration is not registered");
                        break;
                    }
                    case 500: {
                        promptError("Internal server error in the event-stream server endpoint.");
                        break;
                    }
                }
            }

            sb.append(String.format("Http Code : %d, Http Status  :%s \n", response.getStatus(), response.getStatusText()));
            sb.append(response.getBody().toString());
            addToScreenLog(sb.toString());
            registerEventStreamListener.setEnabled(!isRegistered);
        } catch (InvalidCredentialsException e) {
            promptError("Invalid Credentials");
        } catch (RestClientException e) {
            promptError(e.getMessage());
        }
    }

    private void promptError(String message) {
        Notification.show(message);//, 1000*10, Notification.Position.TOP_STRETCH);
    }


    private void addToScreenLog(String text) {
        String toadd = textArea.getValue().concat("\n\n").concat(text);
        textArea.setValue(toadd);
    }

}
