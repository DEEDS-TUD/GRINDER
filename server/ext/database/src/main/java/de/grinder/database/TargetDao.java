package de.grinder.database;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * Database access object for {@link Target}.
 *
 *
 */
public class TargetDao {

    /**
     * Returns all TargetEntities in the database.
     *
     * @return Collection of TargetEntities
     */
    public Collection<Target> getAll() {
        final EntityManager em = Database.getEntityManager();
        final Query query = em.createQuery("select t from Target t", Target.class);
        em.getTransaction().begin();
        @SuppressWarnings("unchecked")
        final Collection<Target> targets = query.getResultList();
        em.getTransaction().commit();
        return targets;
    }

    /**
     * Saves the given {@link Target} to the database.
     *
     * @param target
     *          The {@link Target} that should be saved to the database.
     */
    public void save(final Target target) {
        final EntityManager em = Database.getEntityManager();

        em.getTransaction().begin();
        if (!em.contains(target)) {
            em.persist(target);
            em.flush();
        }
        em.getTransaction().commit();
    }

    /**
     * Returns the Target for the given id.
     *
     * @param id
     *          The id of the Target
     * @return The Target with id
     */
    public Target getById(final int id) {
        final EntityManager em = Database.getEntityManager();
        final Target target = em.find(Target.class, id);
        return target;
    }
}
