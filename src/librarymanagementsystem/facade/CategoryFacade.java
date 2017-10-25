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
import librarymanagementsystem.models.Books;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import librarymanagementsystem.facade.exceptions.NonexistentEntityException;
import librarymanagementsystem.models.Category;

/**
 *
 * @author User
 */
public class CategoryFacade implements Serializable {

    public CategoryFacade(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Category category) {
        if (category.getBooksCollection() == null) {
            category.setBooksCollection(new ArrayList<Books>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Books> attachedBooksCollection = new ArrayList<Books>();
            for (Books booksCollectionBooksToAttach : category.getBooksCollection()) {
                booksCollectionBooksToAttach = em.getReference(booksCollectionBooksToAttach.getClass(), booksCollectionBooksToAttach.getBookId());
                attachedBooksCollection.add(booksCollectionBooksToAttach);
            }
            category.setBooksCollection(attachedBooksCollection);
            em.persist(category);
            for (Books booksCollectionBooks : category.getBooksCollection()) {
                booksCollectionBooks.getCategoryCollection().add(category);
                booksCollectionBooks = em.merge(booksCollectionBooks);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Category category) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Category persistentCategory = em.find(Category.class, category.getCategoryId());
            Collection<Books> booksCollectionOld = persistentCategory.getBooksCollection();
            Collection<Books> booksCollectionNew = category.getBooksCollection();
            Collection<Books> attachedBooksCollectionNew = new ArrayList<Books>();
            for (Books booksCollectionNewBooksToAttach : booksCollectionNew) {
                booksCollectionNewBooksToAttach = em.getReference(booksCollectionNewBooksToAttach.getClass(), booksCollectionNewBooksToAttach.getBookId());
                attachedBooksCollectionNew.add(booksCollectionNewBooksToAttach);
            }
            booksCollectionNew = attachedBooksCollectionNew;
            category.setBooksCollection(booksCollectionNew);
            category = em.merge(category);
            for (Books booksCollectionOldBooks : booksCollectionOld) {
                if (!booksCollectionNew.contains(booksCollectionOldBooks)) {
                    booksCollectionOldBooks.getCategoryCollection().remove(category);
                    booksCollectionOldBooks = em.merge(booksCollectionOldBooks);
                }
            }
            for (Books booksCollectionNewBooks : booksCollectionNew) {
                if (!booksCollectionOld.contains(booksCollectionNewBooks)) {
                    booksCollectionNewBooks.getCategoryCollection().add(category);
                    booksCollectionNewBooks = em.merge(booksCollectionNewBooks);
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = category.getCategoryId();
                if (findCategory(id) == null) {
                    throw new NonexistentEntityException("The category with id " + id + " no longer exists.");
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
            Category category;
            try {
                category = em.getReference(Category.class, id);
                category.getCategoryId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The category with id " + id + " no longer exists.", enfe);
            }
            Collection<Books> booksCollection = category.getBooksCollection();
            for (Books booksCollectionBooks : booksCollection) {
                booksCollectionBooks.getCategoryCollection().remove(category);
                booksCollectionBooks = em.merge(booksCollectionBooks);
            }
            em.remove(category);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Category> findCategoryEntities() {
        return findCategoryEntities(true, -1, -1);
    }

    public List<Category> findCategoryEntities(int maxResults, int firstResult) {
        return findCategoryEntities(false, maxResults, firstResult);
    }

    private List<Category> findCategoryEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Category.class));
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

    public Category findCategory(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Category.class, id);
        } finally {
            em.close();
        }
    }

    public int getCategoryCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Category> rt = cq.from(Category.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
