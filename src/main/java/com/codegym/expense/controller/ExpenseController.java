package com.codegym.expense.controller;

import com.codegym.expense.model.Expense;
import com.codegym.expense.model.ExpenseForm;
import com.codegym.expense.service.ExpenseService;
import com.codegym.expense.service.IExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Controller
public class ExpenseController {
    @Autowired
    private IExpenseService expenseService;
    @Value("${file-upload}")
    private String fileUpload;

    @GetMapping("/expense")
    public ModelAndView listExpense() {
        List<Expense> expenses = ExpenseService.findAll();
        ModelAndView modelAndView = new ModelAndView("/expense/list");
        modelAndView.addObject("expenses", expenses);
        return modelAndView;
    }

    @GetMapping("/create-expense")
    public ModelAndView showCreateForm() {
        ModelAndView modelAndView = new ModelAndView("/expense/create");
        modelAndView.addObject("ExpenseForm", new ExpenseForm());
        return modelAndView;
    }

    @PostMapping("/create-expense")
    public ModelAndView saveExpense(@ModelAttribute("Expense") ExpenseForm ExpenseForm) {
        MultipartFile multipartFile = ExpenseForm.getImage();
        String fileName = multipartFile.getOriginalFilename();
        try {
            FileCopyUtils.copy(ExpenseForm.getImage().getBytes(), new File(fileUpload + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Expense expense = new Expense(ExpenseForm.getId(), ExpenseForm.getName(),
                ExpenseForm.getPrice(), ExpenseForm.getDescription(), fileName);
        ExpenseService.save(expense);
        ModelAndView modelAndView = new ModelAndView("/expense/create");
        modelAndView.addObject("ExpenseForm", new ExpenseForm());
        modelAndView.addObject("message", "New expense created successfully");
        return modelAndView;
    }

    @GetMapping("/edit-expense/{id}")
    public ModelAndView showEditForm(@PathVariable Long id) {
        Expense expense = ExpenseService.findById(id);
        if (expense != null) {
            ModelAndView modelAndView = new ModelAndView("/Expense/edit");
            modelAndView.addObject("expense", expense);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/edit-Expense")
    public ModelAndView updateExpense(@ModelAttribute("Expense") Expense expense) {
        ExpenseService.save(expense);
        ModelAndView modelAndView = new ModelAndView("/expense/edit");
        modelAndView.addObject("expense", expense);
        modelAndView.addObject("message", "Expense updated successfully");
        return modelAndView;
    }

    @GetMapping("/delete-Expense/{id}")
    public ModelAndView showDeleteForm(@PathVariable Long id) {
        Expense expense = ExpenseService.findById(id);
        if (expense != null) {
            ModelAndView modelAndView = new ModelAndView("/expense/delete");
            modelAndView.addObject("expense", expense);
            return modelAndView;
        } else {
            ModelAndView modelAndView = new ModelAndView("/error.404");
            return modelAndView;
        }
    }

    @PostMapping("/delete-Expense")
    public String deleteExpense(@ModelAttribute("Expense") Expense expense) {
        expenseService.remove(expense.getId());
        return "redirect:Expenses";
    }
}
