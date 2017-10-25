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
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.BookOverdue;

/**
 *
 * @author User
 */
public class BookOverdueJpaController implements Serializable {

    public BookOverdueJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookOverdue bookOverdue) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookBorrower bookBorrowerRefId = bookOverdue.getBookBorrowerRefId();
            if (bookBorrowerRefId != null) {
                bookBorrowerRefId = em.getReference(bookBorrowerRefId.getClass(), bookBorrowerRefId.getId());
                bookOverdue.setBookBorrowerRefId(bookBorrowerRefId);
            }
            em.persist(bookOverdue);
            if (bookBorrowerRefId != null) {
                bookBorrowerRefId.getBookOverdueList().add(bookOverdue);
                bookBorrowerRefId = em.merge(bookBorrowerRefId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BookOverdue bookOverdue) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookOverdue persistentBookOverdue = em.find(BookOverdue.class, bookOverdue.getId());
            BookBorrower bookBorrowerRefIdOld = persistentBookOverdue.getBookBorrowerRefId();
            BookBorrower bookBorrowerRefIdNew = bookOverdue.getBookBorrowerRefId();
            if (bookBorrowerRefIdNew != null) {
                bookBorrowerRefIdNew = em.getReference(bookBorrowerRefIdNew.getClass(), bookBorrowerRefIdNew.getId());
                bookOverdue.setBookBorrowerRefId(bookBorrowerRefIdNew);
            }
            bookOverdue = em.merge(bookOverdue);
            if (bookBorrowerRefIdOld != null && !bookBorrowerRefIdOld.equals(bookBorrowerRefIdNew)) {
                bookBorrowerRefIdOld.getBookOverdueList().remove(bookOverdue);
                bookBorrowerRefIdOld = em.merge(bookBorrowerRefIdOld);
            }
            if (bookBorrowerRefIdNew != null && !bookBorrowerRefIdNew.equals(bookBorrowerRefIdOld)) {
                bookBorrowerRefIdNew.getBookOverdueList().add(bookOverdue);
                bookBorrowerRefIdNew = em.merge(bookBorrowerRefIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bookOverdue.getId();
                if (findBookOverdue(id) == null) {
                    throw new NonexistentEntityException("The bookOverdue with id " + id + " no longer exists.");
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
            BookOverdue bookOverdue;
            try {
                bookOverdue = em.getReference(BookOverdue.class, id);
                bookOverdue.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookOverdue with id " + id + " no longer exists.", enfe);
            }
            BookBorrower bookBorrowerRefId = bookOverdue.getBookBorrowerRefId();
            if (bookBorrowerRefId != null) {
                bookBorrowerRefId.getBookOverdueList().remove(bookOverdue);
                bookBorrowerRefId = em.merge(bookBorrowerRefId);
            }
            em.remove(bookOverdue);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BookOverdue> findBookOverdueEntities() {
        return findBookOverdueEntities(true, -1, -1);
    }

    public List<BookOverdue> findBookOverdueEntities(int maxResults, int firstResult) {
        return findBookOverdueEntities(false, maxResults, firstResult);
    }

    private List<BookOverdue> findBookOverdueEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BookOverdue.class));
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

    public BookOverdue findBookOverdue(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookOverdue.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookOverdueCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BookOverdue> rt = cq.from(BookOverdue.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
