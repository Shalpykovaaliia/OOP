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
import librarymanagementsystem.models.Borrower;

/**
 *
 * @author User
 */
public class BookBorrowerJpaController implements Serializable {

    public BookBorrowerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookBorrower bookBorrower) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Borrower borrowerId = bookBorrower.getBorrowerId();
            if (borrowerId != null) {
                borrowerId = em.getReference(borrowerId.getClass(), borrowerId.getBorrowerId());
                bookBorrower.setBorrowerId(borrowerId);
            }
            em.persist(bookBorrower);
            if (borrowerId != null) {
                borrowerId.getBookBorrowerCollection().add(bookBorrower);
                borrowerId = em.merge(borrowerId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(BookBorrower bookBorrower) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookBorrower persistentBookBorrower = em.find(BookBorrower.class, bookBorrower.getId());
            Borrower borrowerIdOld = persistentBookBorrower.getBorrowerId();
            Borrower borrowerIdNew = bookBorrower.getBorrowerId();
            if (borrowerIdNew != null) {
                borrowerIdNew = em.getReference(borrowerIdNew.getClass(), borrowerIdNew.getBorrowerId());
                bookBorrower.setBorrowerId(borrowerIdNew);
            }
            bookBorrower = em.merge(bookBorrower);
            if (borrowerIdOld != null && !borrowerIdOld.equals(borrowerIdNew)) {
                borrowerIdOld.getBookBorrowerCollection().remove(bookBorrower);
                borrowerIdOld = em.merge(borrowerIdOld);
            }
            if (borrowerIdNew != null && !borrowerIdNew.equals(borrowerIdOld)) {
                borrowerIdNew.getBookBorrowerCollection().add(bookBorrower);
                borrowerIdNew = em.merge(borrowerIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = bookBorrower.getId();
                if (findBookBorrower(id) == null) {
                    throw new NonexistentEntityException("The bookBorrower with id " + id + " no longer exists.");
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
            BookBorrower bookBorrower;
            try {
                bookBorrower = em.getReference(BookBorrower.class, id);
                bookBorrower.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The bookBorrower with id " + id + " no longer exists.", enfe);
            }
            Borrower borrowerId = bookBorrower.getBorrowerId();
            if (borrowerId != null) {
                borrowerId.getBookBorrowerCollection().remove(bookBorrower);
                borrowerId = em.merge(borrowerId);
            }
            em.remove(bookBorrower);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<BookBorrower> findBookBorrowerEntities() {
        return findBookBorrowerEntities(true, -1, -1);
    }

    public List<BookBorrower> findBookBorrowerEntities(int maxResults, int firstResult) {
        return findBookBorrowerEntities(false, maxResults, firstResult);
    }

    private List<BookBorrower> findBookBorrowerEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(BookBorrower.class));
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

    public BookBorrower findBookBorrower(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(BookBorrower.class, id);
        } finally {
            em.close();
        }
    }

    public int getBookBorrowerCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<BookBorrower> rt = cq.from(BookBorrower.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
