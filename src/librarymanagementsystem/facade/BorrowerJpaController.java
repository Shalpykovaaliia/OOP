/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.facade;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import librarymanagementsystem.models.BookBorrower;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import librarymanagementsystem.facade.exceptions.IllegalOrphanException;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Borrower;

/**
 *
 * @author User
 */
public class BorrowerJpaController implements Serializable {

    public BorrowerJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Borrower borrower) {
        if (borrower.getBookBorrowerCollection() == null) {
            borrower.setBookBorrowerCollection(new ArrayList<BookBorrower>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<BookBorrower> attachedBookBorrowerCollection = new ArrayList<BookBorrower>();
            for (BookBorrower bookBorrowerCollectionBookBorrowerToAttach : borrower.getBookBorrowerCollection()) {
                bookBorrowerCollectionBookBorrowerToAttach = em.getReference(bookBorrowerCollectionBookBorrowerToAttach.getClass(), bookBorrowerCollectionBookBorrowerToAttach.getId());
                attachedBookBorrowerCollection.add(bookBorrowerCollectionBookBorrowerToAttach);
            }
            borrower.setBookBorrowerCollection(attachedBookBorrowerCollection);
            em.persist(borrower);
            for (BookBorrower bookBorrowerCollectionBookBorrower : borrower.getBookBorrowerCollection()) {
                Borrower oldBorrowerIdOfBookBorrowerCollectionBookBorrower = bookBorrowerCollectionBookBorrower.getBorrowerId();
                bookBorrowerCollectionBookBorrower.setBorrowerId(borrower);
                bookBorrowerCollectionBookBorrower = em.merge(bookBorrowerCollectionBookBorrower);
                if (oldBorrowerIdOfBookBorrowerCollectionBookBorrower != null) {
                    oldBorrowerIdOfBookBorrowerCollectionBookBorrower.getBookBorrowerCollection().remove(bookBorrowerCollectionBookBorrower);
                    oldBorrowerIdOfBookBorrowerCollectionBookBorrower = em.merge(oldBorrowerIdOfBookBorrowerCollectionBookBorrower);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Borrower borrower) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Borrower persistentBorrower = em.find(Borrower.class, borrower.getBorrowerId());
            Collection<BookBorrower> bookBorrowerCollectionOld = persistentBorrower.getBookBorrowerCollection();
            Collection<BookBorrower> bookBorrowerCollectionNew = borrower.getBookBorrowerCollection();
            List<String> illegalOrphanMessages = null;
            for (BookBorrower bookBorrowerCollectionOldBookBorrower : bookBorrowerCollectionOld) {
                if (!bookBorrowerCollectionNew.contains(bookBorrowerCollectionOldBookBorrower)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookBorrower " + bookBorrowerCollectionOldBookBorrower + " since its borrowerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<BookBorrower> attachedBookBorrowerCollectionNew = new ArrayList<BookBorrower>();
            for (BookBorrower bookBorrowerCollectionNewBookBorrowerToAttach : bookBorrowerCollectionNew) {
                bookBorrowerCollectionNewBookBorrowerToAttach = em.getReference(bookBorrowerCollectionNewBookBorrowerToAttach.getClass(), bookBorrowerCollectionNewBookBorrowerToAttach.getId());
                attachedBookBorrowerCollectionNew.add(bookBorrowerCollectionNewBookBorrowerToAttach);
            }
            bookBorrowerCollectionNew = attachedBookBorrowerCollectionNew;
            borrower.setBookBorrowerCollection(bookBorrowerCollectionNew);
            borrower = em.merge(borrower);
            for (BookBorrower bookBorrowerCollectionNewBookBorrower : bookBorrowerCollectionNew) {
                if (!bookBorrowerCollectionOld.contains(bookBorrowerCollectionNewBookBorrower)) {
                    Borrower oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower = bookBorrowerCollectionNewBookBorrower.getBorrowerId();
                    bookBorrowerCollectionNewBookBorrower.setBorrowerId(borrower);
                    bookBorrowerCollectionNewBookBorrower = em.merge(bookBorrowerCollectionNewBookBorrower);
                    if (oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower != null && !oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower.equals(borrower)) {
                        oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower.getBookBorrowerCollection().remove(bookBorrowerCollectionNewBookBorrower);
                        oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower = em.merge(oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower);
                    }
                }
            }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<BookBorrower> bookBorrowerCollectionOrphanCheck = borrower.getBookBorrowerCollection();
            for (BookBorrower bookBorrowerCollectionOrphanCheckBookBorrower : bookBorrowerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Borrower (" + borrower + ") cannot be destroyed since the BookBorrower " + bookBorrowerCollectionOrphanCheckBookBorrower + " in its bookBorrowerCollection field has a non-nullable borrowerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
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
