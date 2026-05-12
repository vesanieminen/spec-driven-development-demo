package com.example.specdriven.tickets.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.router.RouterLayout;

public class MainLayout extends AppLayout implements RouterLayout {
    public MainLayout() {
        addClassName("qt-app");

        Div header = new Div();
        header.addClassName("qt-desktop-nav");

        Span brand = new Span("QUICK TRANSIT");
        brand.addClassName("qt-brand");
        header.add(brand);

        addToNavbar(header);
    }
}

