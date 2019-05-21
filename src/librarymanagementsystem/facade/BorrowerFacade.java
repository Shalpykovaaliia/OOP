/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.facade;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import librarymanagementsystem.facade.exceptions.IllegalOrphanException;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.BookBorrower;
import librarymanagementsystem.models.BookOverdue;
import librarymanagementsystem.models.Books;
import librarymanagementsystem.models.Borrower;
import librarymanagementsystem.models.SmsNotificationLog;
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

    public Borrower findBorrowerByBarcode(Double borrowerBarcode) {
        System.out.println("looking for borrower with ID : "+borrowerBarcode.toString());
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

    public void deleteByBorrowerBarcode(Double borrowerBarcode) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Borrower borrower;
            // get the borrower by barcode
            TypedQuery<Borrower> query = em.createNamedQuery("Borrower.findByBarcode", Borrower.class);
            query.setParameter("borrowerBarcode", borrowerBarcode);
            borrower = query.getSingleResult();
            if (borrower == null) {
                throw new NonexistentEntityException("Can't find borrower with barcode " + borrowerBarcode.toString());
            }
            //delete the current borrower record
            borrower = em.getReference(Borrower.class, borrower.getBorrowerId());
            borrower.getBorrowerId();
            em.remove(borrower);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public Borrower getRecordByBarcodeId(Double borrowerBarcode) throws NonexistentEntityException {
        EntityManager em = null;
        Borrower borrower = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            // get the borrower by barcode
            TypedQuery<Borrower> query = em.createNamedQuery("Borrower.findByBarcode", Borrower.class);
            query.setHint("javax.persistence.cache.storeMode", "REFRESH");
            query.setHint(QueryHints.REFRESH, HintValues.TRUE);
            query.setParameter("borrowerBarcode", borrowerBarcode);
            borrower = query.getSingleResult();
            if (borrower == null) {
                throw new NonexistentEntityException("Can't find borrower with barcode " + borrowerBarcode.toString());
            }
            em.getTransaction().commit();

        } finally {
            if (em != null) {
                em.close();
            }
            return borrower;
        }
    }

    public boolean hasOverDuedBooks(Borrower borrowerModel) {
        //Get all overdued book and check if borrower is in the list
        EntityManager em = getEntityManager();
        TypedQuery<BookBorrower> query = em.createNamedQuery("BookBorrower.borrowerHasOverduedBook", BookBorrower.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        query.setParameter("borrowerId", borrowerModel);
        List<BookBorrower> result = query.getResultList();
        Logger.getLogger(BorrowerFacade.class.getName()).log(Level.INFO, "Has overdued book " + result.size());
        return result.size() > 0;
    }

    public void deleteChildren(Borrower borrowerMdl) throws IllegalOrphanException, NonexistentEntityException, Exception {
        BookOverdueJpaController bookOverdueFacade = new BookOverdueJpaController(emf);
        BookBorrowerJpaController bookBorrowerFacade = new BookBorrowerJpaController(emf);
        BooksFacade booksFacade = new BooksFacade(emf);
        SmsNotificationLogFacade smsNotiflogFacade = new SmsNotificationLogFacade(emf);
        List<BookBorrower> bookBorrowerColl = (List<BookBorrower>) borrowerMdl.getBookBorrowerCollection();
        for (Iterator<BookBorrower> iterator = bookBorrowerColl.iterator(); iterator.hasNext();) {
            BookBorrower currentBookBorrower = iterator.next();
            //delete sms notif log records as a consequence of deleting the record
            List<SmsNotificationLog> smsNotifRecorsd = (List<SmsNotificationLog>) currentBookBorrower.getSmsNotificationLogCollection();
            for (Iterator<SmsNotificationLog> iterator1 = smsNotifRecorsd.iterator(); iterator1.hasNext();) {
                SmsNotificationLog curSmsNotifRec = iterator1.next();
                smsNotiflogFacade.destroy(curSmsNotifRec.getId());
                Logger.getLogger(BorrowerFacade.class.getName()).log(Level.INFO, "Deleting sms notif record :"+ curSmsNotifRec.getId());
            }
            List<BookOverdue> bookOverdueColl = (List<BookOverdue>) currentBookBorrower.getBookOverdueCollection();
            for (Iterator<BookOverdue> iterator1 = bookOverdueColl.iterator(); iterator1.hasNext();) {
                BookOverdue currentOverdue = iterator1.next();
                bookOverdueFacade.destroy(currentOverdue.getId());
            }
            Books currentBook = currentBookBorrower.getBook();
            currentBook.setAvailability(Books.AVAILABLE_STATUS);
            booksFacade.edit(currentBook);
            Logger.getLogger(BorrowerFacade.class.getName()).log(Level.INFO, "Updating status to available with book id " + currentBook.getBookId());
            bookBorrowerFacade.destroy(currentBookBorrower.getId());
        }
    }
}
