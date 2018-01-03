/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarymanagementsystem.components;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import librarymanagementsystem.models.BookBorrower;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

/**
 *
 * @author User
 */
public class OverdueRecordReport {

    private EntityManager em;
    private EntityManagerFactory emf;

    public OverdueRecordReport(EntityManager em, EntityManagerFactory emf) {
        this.em = em;
        this.emf = emf;
    }

    
    // @TODO
    public List<BookBorrower> getOverdueReport() {
        TypedQuery<BookBorrower> query = this.em.createNamedQuery("BookBorrower.findOverduedBook", BookBorrower.class);
        query.setHint("javax.persistence.cache.storeMode", "REFRESH");
        query.setHint(QueryHints.REFRESH, HintValues.TRUE);
        return query.getResultList();
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

    public EntityManagerFactory getEmf() {
        return emf;
    }

    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }

}
