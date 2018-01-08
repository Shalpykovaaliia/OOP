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
import librarymanagementsystem.models.SmsNotificationLog;

/**
 *
 * @author User
 */
public class SmsNotificationLogFacade implements Serializable {

    public SmsNotificationLogFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(SmsNotificationLog smsNotificationLog) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            BookBorrower bookBorrowerId = smsNotificationLog.getBookBorrowerId();
            if (bookBorrowerId != null) {
                bookBorrowerId = em.getReference(bookBorrowerId.getClass(), bookBorrowerId.getId());
                smsNotificationLog.setBookBorrowerId(bookBorrowerId);
            }
            em.persist(smsNotificationLog);
            if (bookBorrowerId != null) {
                bookBorrowerId.getSmsNotificationLogCollection().add(smsNotificationLog);
                bookBorrowerId = em.merge(bookBorrowerId);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(SmsNotificationLog smsNotificationLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            SmsNotificationLog persistentSmsNotificationLog = em.find(SmsNotificationLog.class, smsNotificationLog.getId());
            BookBorrower bookBorrowerIdOld = persistentSmsNotificationLog.getBookBorrowerId();
            BookBorrower bookBorrowerIdNew = smsNotificationLog.getBookBorrowerId();
            if (bookBorrowerIdNew != null) {
                bookBorrowerIdNew = em.getReference(bookBorrowerIdNew.getClass(), bookBorrowerIdNew.getId());
                smsNotificationLog.setBookBorrowerId(bookBorrowerIdNew);
            }
            smsNotificationLog = em.merge(smsNotificationLog);
            if (bookBorrowerIdOld != null && !bookBorrowerIdOld.equals(bookBorrowerIdNew)) {
                bookBorrowerIdOld.getSmsNotificationLogCollection().remove(smsNotificationLog);
                bookBorrowerIdOld = em.merge(bookBorrowerIdOld);
            }
            if (bookBorrowerIdNew != null && !bookBorrowerIdNew.equals(bookBorrowerIdOld)) {
                bookBorrowerIdNew.getSmsNotificationLogCollection().add(smsNotificationLog);
                bookBorrowerIdNew = em.merge(bookBorrowerIdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = smsNotificationLog.getId();
                if (findSmsNotificationLog(id) == null) {
                    throw new NonexistentEntityException("The smsNotificationLog with id " + id + " no longer exists.");
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
            SmsNotificationLog smsNotificationLog;
            try {
                smsNotificationLog = em.getReference(SmsNotificationLog.class, id);
                smsNotificationLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The smsNotificationLog with id " + id + " no longer exists.", enfe);
            }
            BookBorrower bookBorrowerId = smsNotificationLog.getBookBorrowerId();
            if (bookBorrowerId != null) {
                bookBorrowerId.getSmsNotificationLogCollection().remove(smsNotificationLog);
                bookBorrowerId = em.merge(bookBorrowerId);
            }
            em.remove(smsNotificationLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<SmsNotificationLog> findSmsNotificationLogEntities() {
        return findSmsNotificationLogEntities(true, -1, -1);
    }

    public List<SmsNotificationLog> findSmsNotificationLogEntities(int maxResults, int firstResult) {
        return findSmsNotificationLogEntities(false, maxResults, firstResult);
    }

    private List<SmsNotificationLog> findSmsNotificationLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(SmsNotificationLog.class));
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

    public SmsNotificationLog findSmsNotificationLog(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(SmsNotificationLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getSmsNotificationLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<SmsNotificationLog> rt = cq.from(SmsNotificationLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

}
