package com.desafiotecnico.ponta.cesarlopes;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        System.out.printf("App Event: %s%n", event);
        applicationContext
                .getBean(RequestMappingHandlerMapping.class)
                .getHandlerMethods()
                .forEach((t, m) -> System.out.printf("Endpoint: %s -> %s%n", t, m));
    }
}