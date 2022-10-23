package com.codegym.expense.repository;

import com.codegym.expense.model.Expense;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

public class ExpenseRepository implements IExpenseRepository{
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Expense> findAll() {
        TypedQuery<Expense> query = em.createQuery("select c from Expense c", Expense.class);
        return query.getResultList();
    }

    @Override
    public Expense findById(Long id) {
        TypedQuery<Expense> query = em.createQuery("select c from Expense c where c.id=:id", Expense.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public void save(Expense expense) {
        if (expense.getId() != null) {
            em.merge(expense);
        } else {
            em.persist(expense);
        }
    }

    @Override
    public void remove(Long id) {
        Expense expense = findById(id);
        if (expense != null) {
            em.remove(expense);
        }
    }
}