package com.codegym.expense.service;

import com.codegym.expense.model.Expense;
import com.codegym.expense.repository.IExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ExpenseService implements IExpenseService{
    @Autowired
    private IExpenseRepository expenseRepository;

    @Override
    public static List<Expense> findAll() {
        return expenseRepository.findAll();
    }

    @Override
    public static Expense findById(Long id) {
        return expenseRepository.findById(id);
    }

    @Override
    public static void save(Expense expense) {
        expenseRepository.save(expense);
    }

    @Override
    public void remove(Long id) {
        expenseRepository.remove(id);
    }
}
