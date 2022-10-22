package ua.dragunov.lightshow.repository;

import jakarta.persistence.EntityTransaction;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Light;

public class LightRepositoryImpl implements LightRepository{
    private final SessionFactory sessionFactory;

    public LightRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Light light) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(light);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }

    }

    @Override
    public Light findByLabel(String label) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Query<Light> query = session.createQuery("from Light l join fetch l.color where l.label = :label",Light.class)
                            .setParameter("label", label);

            Light light = query.uniqueResult();

            transaction.commit();

            return light;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }

    }

    @Override
    public void update(Light light) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.merge(light);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }


    }

    @Override
    public void remove(Light light) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.remove(light);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }

    }
}
