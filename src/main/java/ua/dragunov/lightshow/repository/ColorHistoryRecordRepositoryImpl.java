package ua.dragunov.lightshow.repository;

import jakarta.persistence.EntityTransaction;
import org.hibernate.SessionFactory;
import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.ColorHistoryRecord;

public class ColorHistoryRecordRepositoryImpl implements ColorHistoryRecordRepository{
    private final SessionFactory sessionFactory;

    public ColorHistoryRecordRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(ColorHistoryRecord colorHistoryRecord) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(colorHistoryRecord);

            transaction.commit();


        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }
    }

    @Override
    public ColorHistoryRecord findById(Long id) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            ColorHistoryRecord colorHistoryRecord = session.find(ColorHistoryRecord.class, id);

            transaction.commit();

            return colorHistoryRecord;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }


    }

    @Override
    public void remove(ColorHistoryRecord colorHistoryRecord) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.remove(colorHistoryRecord);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }


    }
}
