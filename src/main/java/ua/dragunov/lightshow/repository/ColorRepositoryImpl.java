package ua.dragunov.lightshow.repository;

import jakarta.persistence.EntityTransaction;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import ua.dragunov.lightshow.exceptions.LightShowException;
import ua.dragunov.lightshow.model.Color;

import java.util.List;

public class ColorRepositoryImpl implements ColorRepository {
    private final SessionFactory sessionFactory;

    public ColorRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(Color color) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.persist(color);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }

    }

    @Override
    public Color findByName(String name) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Query<Color> query = session.createQuery("from Color where name = :name",Color.class)
                    .setParameter("name", name);

            Color color = query.uniqueResult();

            transaction.commit();

            return color;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }

    }

    @Override
    public List<Color> findAll() throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Query<Color> query = session.createQuery("from Color",Color.class);

            List<Color> colors = query.getResultList();

            transaction.commit();

            return colors;
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }

    }

    @Override
    public void remove(Color color) throws LightShowException {
        EntityTransaction transaction = null;

        try(var session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            session.remove(color);

            transaction.commit();

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            throw new LightShowException(e);
        }


    }
}
