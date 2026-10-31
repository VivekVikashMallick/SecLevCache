package org.example;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class CacheDemo {
    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        // Optional: Save a test user first if your database is empty
        Session saveSession = factory.openSession();
        Transaction saveTx = saveSession.beginTransaction();
        User newUser = new User("Alice");
        saveSession.persist(newUser);
        saveTx.commit();
        saveSession.close();

        // --- FIRST SESSION (Cache Miss - Will execute SQL query) ---
        Session session1 = factory.openSession();
        Transaction tx1 = session1.beginTransaction();

        User user1 = session1.get(User.class, 1L);
        System.out.println("Session 1 Result: " + user1);

        tx1.commit();
        session1.close();

        System.out.println("--- Closing first session & looking up via Cache ---");

        // --- SECOND SESSION (Cache Hit - NO SQL query will execute) ---
        Session session2 = factory.openSession();
        Transaction tx2 = session2.beginTransaction();

        User user2 = session2.get(User.class, 1L);
        System.out.println("Session 2 Result: " + user2);

        tx2.commit();
        session2.close();

        // Shutdown factory
        factory.close();
    }
}
