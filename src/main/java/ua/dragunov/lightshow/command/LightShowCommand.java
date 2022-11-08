package ua.dragunov.lightshow.command;

import jakarta.persistence.EntityTransaction;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ua.dragunov.lightshow.command.data.CreateLightShowRequest;
import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Color;
import ua.dragunov.lightshow.model.ColorHistoryRecord;
import ua.dragunov.lightshow.model.Light;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class LightShowCommand implements Command<String>{
    private static final Logger colors = LogManager.getLogger(LightShowCommand.class.getName());
    private final SessionFactory sessionFactory;
    private final CreateLightShowRequest context;

    public LightShowCommand(SessionFactory sessionFactory, CreateLightShowRequest context) {
        this.sessionFactory = sessionFactory;
        this.context = context;
    }

    @Override
    public String execute() throws LightShowException {
        StringBuilder switchingHistory = new StringBuilder();
        Transaction transaction = null;
        try(var session = sessionFactory.openSession()) {
            List<Color> allColorsByUser = findAllColorsByUser();

            transaction = session.beginTransaction();

            Light light = session.createQuery("from Light l join fetch l.color where l.label = :label",Light.class)
                    .setParameter("label", context.label()).uniqueResult();

            if (light == null) {
                light = new Light();
                light.setLabel(context.label());
                light.setColor(getRandomColor(light, allColorsByUser));
                light.setEnabled(false);
                session.persist(light);
            }

            if (light.isEnabled() == true) {
                throw new LightShowException("light already enabled, please turn off");
            }

            if (context.switchingInterval() < 1) {
                throw new LightShowException("switching interval less than 1 second");
            }

            if (context.amountSwitching() < 1) {
                throw new LightShowException("amount switching less than 1 ");
            }

            light.setEnabled(true);

            switchingHistory.append(String.format("Light %s changed color '%s'", light.getLabel(), light.getColor().getName()));

            List<ColorHistoryRecord> colorHistoryRecords = new ArrayList<>();
            for (int i = 0; i < context.amountSwitching(); i++) {
                try {
                    Color randomColor = getRandomColor(light, allColorsByUser);
                    ColorHistoryRecord colorHistoryRecord = new ColorHistoryRecord();
                    colorHistoryRecord.setLight(light);
                    colorHistoryRecord.setOldColor(light.getColor());
                    colorHistoryRecord.setNewColor(randomColor);
                    colorHistoryRecord.setChangedAt(Instant.now());

                    light.setColor(randomColor);
                    switchingHistory.append(String.format(" => '%s'", light.getColor().getName()));
                    colorHistoryRecords.add(colorHistoryRecord);
                    colors.info(String.format("Light '%s' changed color from ‘%s’ to ‘%s’ at %s", light.getLabel()
                            , colorHistoryRecord.getOldColor().getName()
                            , colorHistoryRecord.getNewColor().getName()
                            , Instant
                                    .now().toString()));

                    Thread.sleep(context.switchingInterval() * 1000);

                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            light.setEnabled(false);


            colorHistoryRecords.forEach(colorHistoryRecord -> session.persist(colorHistoryRecord));

            transaction.commit();

            return switchingHistory.toString();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }
    }

    List<Color> findAllColorsByUser() throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Query<Color> query = session.createQuery("from Color",Color.class);

            List<Color> colors = query.getResultList();

            transaction.commit();

            List<Color> colorsByUser = new ArrayList<>();
            for (String colorName : context.colorList()) {
                colorsByUser.add(colors.stream()
                        .filter(color -> color.getName().equals(colorName))
                        .findFirst()
                        .orElseThrow(() -> new LightShowException(String.format("Color name %s does not exist!!", colorName))));
            }

            return colorsByUser;

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }


    }

    Color getRandomColor(Light light, List<Color> colors) {
        List<Color> randomColors = new ArrayList<>(colors);
        randomColors.remove(light.getColor());
        return colors.get(ThreadLocalRandom.current().nextInt(colors.size()));
    }
}
