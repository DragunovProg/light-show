package ua.dragunov.lightshow.command;

import org.hibernate.SessionFactory;
import ua.dragunov.lightshow.command.data.CreateLightShowRequest;


public class CommandFactory {
    private final SessionFactory sessionFactory;
    public CommandFactory(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }


    public Command<String> newLightShowCommand(CreateLightShowRequest context) {
        return new LightShowCommand(sessionFactory, context);
    }
}
