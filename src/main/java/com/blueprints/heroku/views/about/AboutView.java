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

        Image img = new Image("../static/images/empty-plant.png", "placeholder plant");
        img.setWidth("200px");
        add(img);

        String info = applicationInfoProvider.getInfo();
        add(new H2("This place intentionally left empty"));
        add(new Paragraph("It’s a place where you can grow your own UI 🤗"));
        add(new Paragraph(info));
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        getStyle().set("text-align", "center");
    }

}
