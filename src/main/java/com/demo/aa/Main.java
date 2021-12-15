package com.demo.aa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class Main {

    private static final AtomicLong lastId = new AtomicLong(0);

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Hibernate5Module());
        objectMapper.getRegisteredModuleIds().forEach(System.out::println);

        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();

        // Create MetadataSources
        MetadataSources sources = new MetadataSources(registry);

        // Create Metadata
        Metadata metadata = sources.getMetadataBuilder().build();

        // Create SessionFactory
        SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();

        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            var role = new Role(1L, "Role1");
            session.persist(role);
            session.persist(new User(lastId.incrementAndGet(), role, null));
            session.persist(new User(lastId.incrementAndGet(), null, null));
            transaction.commit();
        }

        List<User> result;
        try (var session = sessionFactory.openSession()) {
            result = session.createQuery("SELECT u FROM User u", User.class).list();
        }

        if (result.size() > 1) {
            System.out.println("List(0).role: " + Hibernate.isPropertyInitialized(result.get(0), "role"));
            System.out.println("List(0).data: " + Hibernate.isPropertyInitialized(result.get(0), "data"));

            System.out.println("List(1).role: " + Hibernate.isPropertyInitialized(result.get(1), "role"));
            System.out.println("List(1).data: " + Hibernate.isPropertyInitialized(result.get(1), "data"));
        }

        var finalResult = objectMapper.writeValueAsString(result);
        System.out.println(finalResult);
    }
}
