package de.grinder.database;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 *
 *
 */
public class Database {
    private static EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("grinder");

    private static Database instance = new Database();

    public static EntityManager getEntityManager() {
        return entityManagerFactory.createEntityManager();
    }

    public static Database instance() {
        return instance;
    }

    public void addTarget(final Target target) {
        new TargetDao().save(target);
    }

    public Collection<Target> getTargets() {
        return new TargetDao().getAll();
    }

    public Collection<Campaign> getCampaigns() {
        return new CampaignDao().getAll();
    }
}
