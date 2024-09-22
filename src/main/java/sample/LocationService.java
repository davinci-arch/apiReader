package sample;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;

public class LocationService implements LocationRepository {

    private Queue<Session> sessions = new ArrayBlockingQueue<>(1);

    public LocationService(int initValue) {
        sessions.add(HibernateUtil.getSessionFactory().openSession());
    }

    @Override
    public List<Location> findAll() {
        Session session = sessions.poll();

        session.beginTransaction();
        List<Location> from_location = session.createQuery("from Location", Location.class).list();
        session.getTransaction().commit();
        sessions.offer(session);

        return from_location;
    }

    @Override
    public Optional<Location> getById(Long id) {
        Session session = sessions.poll();
        session.beginTransaction();
        Location location = session.get(Location.class, id);
        session.getTransaction().commit();
        sessions.offer(session);

        return Optional.of(location);
    }

    @Override
    public Optional<Location> getByLocationIp(String ip) {
        Session session = sessions.poll();

        session.beginTransaction();
        String hql = "from Location where ip = :ip";
        Location location = session.createQuery(hql, Location.class)
                .setParameter("ip", ip).uniqueResult();
        session.getTransaction().commit();
        sessions.offer(session);
        return Optional.of(location);
    }

    @Override
    public void save(Location o) {
        Session session = sessions.poll();
        session.beginTransaction();
        session.persist(o);
        session.getTransaction().commit();
        sessions.offer(session);
    }

    @Override
    public void delete(Location o) {
        Session session = sessions.poll();
        session.beginTransaction();
        session.remove(o);
        session.getTransaction().commit();
        sessions.offer(session);
    }

    @Override
    public void deleteById(String id) {
        Session session = sessions.poll();
        session.beginTransaction();
        Location location = session.get(Location.class, id);
        session.remove(location);
        session.getTransaction().commit();
        sessions.offer(session);
    }
}
