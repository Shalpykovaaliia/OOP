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
import librarymanagementsystem.models.BookOverdue;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import librarymanagementsystem.facade.exceptions.IllegalOrphanException;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;

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
        if (bookBorrower.getBookOverdueList() == null) {
            bookBorrower.setBookOverdueList(new ArrayList<BookOverdue>());
        }
        if (bookBorrower.getBookBorrowerList() == null) {
            bookBorrower.setBookBorrowerList(new ArrayList<BookBorrower>());
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
            List<BookOverdue> attachedBookOverdueList = new ArrayList<BookOverdue>();
            for (BookOverdue bookOverdueListBookOverdueToAttach : bookBorrower.getBookOverdueList()) {
                bookOverdueListBookOverdueToAttach = em.getReference(bookOverdueListBookOverdueToAttach.getClass(), bookOverdueListBookOverdueToAttach.getId());
                attachedBookOverdueList.add(bookOverdueListBookOverdueToAttach);
            }
            bookBorrower.setBookOverdueList(attachedBookOverdueList);
            List<BookBorrower> attachedBookBorrowerList = new ArrayList<BookBorrower>();
            for (BookBorrower bookBorrowerListBookBorrowerToAttach : bookBorrower.getBookBorrowerList()) {
                bookBorrowerListBookBorrowerToAttach = em.getReference(bookBorrowerListBookBorrowerToAttach.getClass(), bookBorrowerListBookBorrowerToAttach.getId());
                attachedBookBorrowerList.add(bookBorrowerListBookBorrowerToAttach);
            }
            bookBorrower.setBookBorrowerList(attachedBookBorrowerList);
            em.persist(bookBorrower);
            if (borrowerId != null) {
                borrowerId.getBookBorrowerList().add(bookBorrower);
                borrowerId = em.merge(borrowerId);
            }
            for (BookOverdue bookOverdueListBookOverdue : bookBorrower.getBookOverdueList()) {
                BookBorrower oldBookBorrowerRefIdOfBookOverdueListBookOverdue = bookOverdueListBookOverdue.getBookBorrowerRefId();
                bookOverdueListBookOverdue.setBookBorrowerRefId(bookBorrower);
                bookOverdueListBookOverdue = em.merge(bookOverdueListBookOverdue);
                if (oldBookBorrowerRefIdOfBookOverdueListBookOverdue != null) {
                    oldBookBorrowerRefIdOfBookOverdueListBookOverdue.getBookOverdueList().remove(bookOverdueListBookOverdue);
                    oldBookBorrowerRefIdOfBookOverdueListBookOverdue = em.merge(oldBookBorrowerRefIdOfBookOverdueListBookOverdue);
                }
            }
            for (BookBorrower bookBorrowerListBookBorrower : bookBorrower.getBookBorrowerList()) {
                BookBorrower oldBorrowerIdOfBookBorrowerListBookBorrower = bookBorrowerListBookBorrower.getBorrowerId();
                bookBorrowerListBookBorrower.setBorrowerId(bookBorrower);
                bookBorrowerListBookBorrower = em.merge(bookBorrowerListBookBorrower);
                if (oldBorrowerIdOfBookBorrowerListBookBorrower != null) {
                    oldBorrowerIdOfBookBorrowerListBookBorrower.getBookBorrowerList().remove(bookBorrowerListBookBorrower);
                    oldBorrowerIdOfBookBorrowerListBookBorrower = em.merge(oldBorrowerIdOfBookBorrowerListBookBorrower);
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
            List<BookOverdue> bookOverdueListOld = persistentBookBorrower.getBookOverdueList();
            List<BookOverdue> bookOverdueListNew = bookBorrower.getBookOverdueList();
            List<BookBorrower> bookBorrowerListOld = persistentBookBorrower.getBookBorrowerList();
            List<BookBorrower> bookBorrowerListNew = bookBorrower.getBookBorrowerList();
            List<String> illegalOrphanMessages = null;
            for (BookOverdue bookOverdueListOldBookOverdue : bookOverdueListOld) {
                if (!bookOverdueListNew.contains(bookOverdueListOldBookOverdue)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookOverdue " + bookOverdueListOldBookOverdue + " since its bookBorrowerRefId field is not nullable.");
                }
            }
            for (BookBorrower bookBorrowerListOldBookBorrower : bookBorrowerListOld) {
                if (!bookBorrowerListNew.contains(bookBorrowerListOldBookBorrower)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain BookBorrower " + bookBorrowerListOldBookBorrower + " since its borrowerId field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (borrowerIdNew != null) {
                borrowerIdNew = em.getReference(borrowerIdNew.getClass(), borrowerIdNew.getId());
                bookBorrower.setBorrowerId(borrowerIdNew);
            }
            List<BookOverdue> attachedBookOverdueListNew = new ArrayList<BookOverdue>();
            for (BookOverdue bookOverdueListNewBookOverdueToAttach : bookOverdueListNew) {
                bookOverdueListNewBookOverdueToAttach = em.getReference(bookOverdueListNewBookOverdueToAttach.getClass(), bookOverdueListNewBookOverdueToAttach.getId());
                attachedBookOverdueListNew.add(bookOverdueListNewBookOverdueToAttach);
            }
            bookOverdueListNew = attachedBookOverdueListNew;
            bookBorrower.setBookOverdueList(bookOverdueListNew);
            List<BookBorrower> attachedBookBorrowerListNew = new ArrayList<BookBorrower>();
            for (BookBorrower bookBorrowerListNewBookBorrowerToAttach : bookBorrowerListNew) {
                bookBorrowerListNewBookBorrowerToAttach = em.getReference(bookBorrowerListNewBookBorrowerToAttach.getClass(), bookBorrowerListNewBookBorrowerToAttach.getId());
                attachedBookBorrowerListNew.add(bookBorrowerListNewBookBorrowerToAttach);
            }
            bookBorrowerListNew = attachedBookBorrowerListNew;
            bookBorrower.setBookBorrowerList(bookBorrowerListNew);
            bookBorrower = em.merge(bookBorrower);
            if (borrowerIdOld != null && !borrowerIdOld.equals(borrowerIdNew)) {
                borrowerIdOld.getBookBorrowerList().remove(bookBorrower);
                borrowerIdOld = em.merge(borrowerIdOld);
            }
            if (borrowerIdNew != null && !borrowerIdNew.equals(borrowerIdOld)) {
                borrowerIdNew.getBookBorrowerList().add(bookBorrower);
                borrowerIdNew = em.merge(borrowerIdNew);
            }
            for (BookOverdue bookOverdueListNewBookOverdue : bookOverdueListNew) {
                if (!bookOverdueListOld.contains(bookOverdueListNewBookOverdue)) {
                    BookBorrower oldBookBorrowerRefIdOfBookOverdueListNewBookOverdue = bookOverdueListNewBookOverdue.getBookBorrowerRefId();
                    bookOverdueListNewBookOverdue.setBookBorrowerRefId(bookBorrower);
                    bookOverdueListNewBookOverdue = em.merge(bookOverdueListNewBookOverdue);
                    if (oldBookBorrowerRefIdOfBookOverdueListNewBookOverdue != null && !oldBookBorrowerRefIdOfBookOverdueListNewBookOverdue.equals(bookBorrower)) {
                        oldBookBorrowerRefIdOfBookOverdueListNewBookOverdue.getBookOverdueList().remove(bookOverdueListNewBookOverdue);
                        oldBookBorrowerRefIdOfBookOverdueListNewBookOverdue = em.merge(oldBookBorrowerRefIdOfBookOverdueListNewBookOverdue);
                    }
                }
            }
            for (BookBorrower bookBorrowerListNewBookBorrower : bookBorrowerListNew) {
                if (!bookBorrowerListOld.contains(bookBorrowerListNewBookBorrower)) {
                    BookBorrower oldBorrowerIdOfBookBorrowerListNewBookBorrower = bookBorrowerListNewBookBorrower.getBorrowerId();
                    bookBorrowerListNewBookBorrower.setBorrowerId(bookBorrower);
                    bookBorrowerListNewBookBorrower = em.merge(bookBorrowerListNewBookBorrower);
                    if (oldBorrowerIdOfBookBorrowerListNewBookBorrower != null && !oldBorrowerIdOfBookBorrowerListNewBookBorrower.equals(bookBorrower)) {
                        oldBorrowerIdOfBookBorrowerListNewBookBorrower.getBookBorrowerList().remove(bookBorrowerListNewBookBorrower);
                        oldBorrowerIdOfBookBorrowerListNewBookBorrower = em.merge(oldBorrowerIdOfBookBorrowerListNewBookBorrower);
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
            List<BookOverdue> bookOverdueListOrphanCheck = bookBorrower.getBookOverdueList();
            for (BookOverdue bookOverdueListOrphanCheckBookOverdue : bookOverdueListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BookBorrower (" + bookBorrower + ") cannot be destroyed since the BookOverdue " + bookOverdueListOrphanCheckBookOverdue + " in its bookOverdueList field has a non-nullable bookBorrowerRefId field.");
            }
            List<BookBorrower> bookBorrowerListOrphanCheck = bookBorrower.getBookBorrowerList();
            for (BookBorrower bookBorrowerListOrphanCheckBookBorrower : bookBorrowerListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This BookBorrower (" + bookBorrower + ") cannot be destroyed since the BookBorrower " + bookBorrowerListOrphanCheckBookBorrower + " in its bookBorrowerList field has a non-nullable borrowerId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            BookBorrower borrowerId = bookBorrower.getBorrowerId();
            if (borrowerId != null) {
                borrowerId.getBookBorrowerList().remove(bookBorrower);
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
