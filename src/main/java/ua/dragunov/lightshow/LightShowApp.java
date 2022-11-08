package ua.dragunov.lightshow;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import ua.dragunov.lightshow.cli.LightShowInteractiveCLI;
import ua.dragunov.lightshow.command.CommandFactory;

public class LightShowApp {
    private final static Logger logger = LogManager.getLogger(LightShowApp.class.getName());

    public static void main(String[] args) {
        new LightShowApp().run();
    }

    public int run() {
        try (ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
             SessionFactory sessionFactory = new Configuration().buildSessionFactory(serviceRegistry)) {


            CommandFactory commandFactory = new CommandFactory(sessionFactory);

            LightShowInteractiveCLI cli = new LightShowInteractiveCLI(commandFactory);

            cli.run();

        } catch (Exception e) {
            logger.error("Error during user interaction", e);
            return -1;
        }
        return 0;
    }

}
