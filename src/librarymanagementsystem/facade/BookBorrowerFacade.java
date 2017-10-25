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

/**
 *
 * @author User
 */
public class BookBorrowerFacade implements Serializable {

    public BookBorrowerFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(BookBorrower bookBorrower) {
        if (bookBorrower.getBookBorrowerCollection() == null) {
            bookBorrower.setBookBorrowerCollection(new ArrayList<BookBorrower>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookBorrower borrowerId = bookBorrower.getBorrowerId();
            if (borrowerId != null) {
                borrowerId = em.getReference(borrowerId.getClass(), borrowerId.getId());
                bookBorrower.setBorrowerId(borrowerId);
            }
            Collection<BookBorrower> attachedBookBorrowerCollection = new ArrayList<BookBorrower>();
            for (BookBorrower bookBorrowerCollectionBookBorrowerToAttach : bookBorrower.getBookBorrowerCollection()) {
                bookBorrowerCollectionBookBorrowerToAttach = em.getReference(bookBorrowerCollectionBookBorrowerToAttach.getClass(), bookBorrowerCollectionBookBorrowerToAttach.getId());
                attachedBookBorrowerCollection.add(bookBorrowerCollectionBookBorrowerToAttach);
            }
            bookBorrower.setBookBorrowerCollection(attachedBookBorrowerCollection);
            em.persist(bookBorrower);
            if (borrowerId != null) {
                borrowerId.getBookBorrowerCollection().add(bookBorrower);
                borrowerId = em.merge(borrowerId);
            }
            for (BookBorrower bookBorrowerCollectionBookBorrower : bookBorrower.getBookBorrowerCollection()) {
                BookBorrower oldBorrowerIdOfBookBorrowerCollectionBookBorrower = bookBorrowerCollectionBookBorrower.getBorrowerId();
                bookBorrowerCollectionBookBorrower.setBorrowerId(bookBorrower);
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

    public void edit(BookBorrower bookBorrower) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookBorrower persistentBookBorrower = em.find(BookBorrower.class, bookBorrower.getId());
            BookBorrower borrowerIdOld = persistentBookBorrower.getBorrowerId();
            BookBorrower borrowerIdNew = bookBorrower.getBorrowerId();
            Collection<BookBorrower> bookBorrowerCollectionOld = persistentBookBorrower.getBookBorrowerCollection();
            Collection<BookBorrower> bookBorrowerCollectionNew = bookBorrower.getBookBorrowerCollection();
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
            if (borrowerIdNew != null) {
                borrowerIdNew = em.getReference(borrowerIdNew.getClass(), borrowerIdNew.getId());
                bookBorrower.setBorrowerId(borrowerIdNew);
            }
            Collection<BookBorrower> attachedBookBorrowerCollectionNew = new ArrayList<BookBorrower>();
            for (BookBorrower bookBorrowerCollectionNewBookBorrowerToAttach : bookBorrowerCollectionNew) {
                bookBorrowerCollectionNewBookBorrowerToAttach = em.getReference(bookBorrowerCollectionNewBookBorrowerToAttach.getClass(), bookBorrowerCollectionNewBookBorrowerToAttach.getId());
                attachedBookBorrowerCollectionNew.add(bookBorrowerCollectionNewBookBorrowerToAttach);
            }
            bookBorrowerCollectionNew = attachedBookBorrowerCollectionNew;
            bookBorrower.setBookBorrowerCollection(bookBorrowerCollectionNew);
            bookBorrower = em.merge(bookBorrower);
            if (borrowerIdOld != null && !borrowerIdOld.equals(borrowerIdNew)) {
                borrowerIdOld.getBookBorrowerCollection().remove(bookBorrower);
                borrowerIdOld = em.merge(borrowerIdOld);
            }
            if (borrowerIdNew != null && !borrowerIdNew.equals(borrowerIdOld)) {
                borrowerIdNew.getBookBorrowerCollection().add(bookBorrower);
                borrowerIdNew = em.merge(borrowerIdNew);
            }
            for (BookBorrower bookBorrowerCollectionNewBookBorrower : bookBorrowerCollectionNew) {
                if (!bookBorrowerCollectionOld.contains(bookBorrowerCollectionNewBookBorrower)) {
                    BookBorrower oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower = bookBorrowerCollectionNewBookBorrower.getBorrowerId();
                    bookBorrowerCollectionNewBookBorrower.setBorrowerId(bookBorrower);
                    bookBorrowerCollectionNewBookBorrower = em.merge(bookBorrowerCollectionNewBookBorrower);
                    if (oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower != null && !oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower.equals(bookBorrower)) {
                        oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower.getBookBorrowerCollection().remove(bookBorrowerCollectionNewBookBorrower);
                        oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower = em.merge(oldBorrowerIdOfBookBorrowerCollectionNewBookBorrower);
                    }
                }
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

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
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
            List<String> illegalOrphanMessages = null;
            Collection<BookBorrower> bookBorrowerCollectionOrphanCheck = bookBorrower.getBookBorrowerCollection();
            for (BookBorrower bookBorrowerCollectionOrphanCheckBookBorrower : bookBorrowerCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BookBorrower (" + bookBorrower + ") cannot be destroyed since the BookBorrower " + bookBorrowerCollectionOrphanCheckBookBorrower + " in its bookBorrowerCollection field has a non-nullable borrowerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            BookBorrower borrowerId = bookBorrower.getBorrowerId();
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
