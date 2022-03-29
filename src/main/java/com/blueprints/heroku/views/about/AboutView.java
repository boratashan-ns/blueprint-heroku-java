package com.blueprints.heroku.views.about;

import com.blueprints.heroku.configuration.ApplicationInfoProvider;
import com.blueprints.heroku.views.MainLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
public class AboutView extends VerticalLayout {

    @Autowired
    public AboutView(ApplicationInfoProvider applicationInfoProvider) {
        setSpacing(false);

        Image img = new Image("../static/images/newstore-logo.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        String info = applicationInfoProvider.getInfo();
        String appName = applicationInfoProvider.getAppName();
        add(new H2("Newstore Heroku Sample Integration APP"));
        add(new Paragraph("*******************************"));
        add(new Paragraph(info));
        add(new Paragraph(appName));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
