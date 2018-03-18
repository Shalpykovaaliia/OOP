/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.facade;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Borrower;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author User
 */
public class BorrowerFacade implements Serializable {

    public BorrowerFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Borrower borrower) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(borrower);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Borrower borrower) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            borrower = em.merge(borrower);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = borrower.getBorrowerId();
                if (findBorrower(id) == null) {
                    throw new NonexistentEntityException("The borrower with id " + id + " no longer exists.");
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
            Borrower borrower;
            try {
                borrower = em.getReference(Borrower.class, id);
                borrower.getBorrowerId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The borrower with id " + id + " no longer exists.", enfe);
            }
            em.remove(borrower);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Borrower> findBorrowerEntities() {
        return findBorrowerEntities(true, -1, -1);
    }

    public List<Borrower> findBorrowerEntities(int maxResults, int firstResult) {
        return findBorrowerEntities(false, maxResults, firstResult);
    }

    private List<Borrower> findBorrowerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Borrower.class));
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

    public Borrower findBorrower(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Borrower.class, id);
        } finally {
            em.close();
        }
    }

    public Borrower findBorrowerByBarcode(Integer borrowerBarcode) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Borrower> query = em.createNamedQuery("Borrower.findByBarcode", Borrower.class);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            query.setParameter("borrowerBarcode", borrowerBarcode);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }

    public int getBorrowerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Borrower> rt = cq.from(Borrower.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
