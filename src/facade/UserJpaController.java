/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facade;

import facade.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import librarymanagementsystem.models.Profile;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import librarymanagementsystem.models.User;

/**
 *
 * @author User
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getProfileCollection() == null) {
            user.setProfileCollection(new ArrayList<Profile>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Profile> attachedProfileCollection = new ArrayList<Profile>();
            for (Profile profileCollectionProfileToAttach : user.getProfileCollection()) {
                profileCollectionProfileToAttach = em.getReference(profileCollectionProfileToAttach.getClass(), profileCollectionProfileToAttach.getProfileId());
                attachedProfileCollection.add(profileCollectionProfileToAttach);
            }
            user.setProfileCollection(attachedProfileCollection);
            em.persist(user);
            for (Profile profileCollectionProfile : user.getProfileCollection()) {
                profileCollectionProfile.getUserCollection().add(user);
                profileCollectionProfile = em.merge(profileCollectionProfile);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Collection<Profile> profileCollectionOld = persistentUser.getProfileCollection();
            Collection<Profile> profileCollectionNew = user.getProfileCollection();
            Collection<Profile> attachedProfileCollectionNew = new ArrayList<Profile>();
            for (Profile profileCollectionNewProfileToAttach : profileCollectionNew) {
                profileCollectionNewProfileToAttach = em.getReference(profileCollectionNewProfileToAttach.getClass(), profileCollectionNewProfileToAttach.getProfileId());
                attachedProfileCollectionNew.add(profileCollectionNewProfileToAttach);
            }
            profileCollectionNew = attachedProfileCollectionNew;
            user.setProfileCollection(profileCollectionNew);
            user = em.merge(user);
            for (Profile profileCollectionOldProfile : profileCollectionOld) {
                if (!profileCollectionNew.contains(profileCollectionOldProfile)) {
                    profileCollectionOldProfile.getUserCollection().remove(user);
                    profileCollectionOldProfile = em.merge(profileCollectionOldProfile);
                }
            }
            for (Profile profileCollectionNewProfile : profileCollectionNew) {
                if (!profileCollectionOld.contains(profileCollectionNewProfile)) {
                    profileCollectionNewProfile.getUserCollection().add(user);
                    profileCollectionNewProfile = em.merge(profileCollectionNewProfile);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Collection<Profile> profileCollection = user.getProfileCollection();
            for (Profile profileCollectionProfile : profileCollection) {
                profileCollectionProfile.getUserCollection().remove(user);
                profileCollectionProfile = em.merge(profileCollectionProfile);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public User findUser(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
