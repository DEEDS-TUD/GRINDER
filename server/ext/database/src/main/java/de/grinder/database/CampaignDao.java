package de.grinder.database;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class CampaignDao {

    public Campaign getById(final Long id) {
        final EntityManager em = Database.getEntityManager();
        final Campaign entity = em.find(Campaign.class, id);
        em.close();
        return entity;
    }

    public Collection<Campaign> getAll() {
        final EntityManager em = Database.getEntityManager();
        final Query query = em.createQuery("select t from Campaign t", Campaign.class);
        em.getTransaction().begin();
        @SuppressWarnings("unchecked")
        final Collection<Campaign> campaigns = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return campaigns;
    }
}
