package ru.chetvertak.TheLibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.services.BooksService;
import ru.chetvertak.TheLibrary.services.PeopleService;

import javax.validation.Valid;

@Controller
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;

    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService) {
        this.peopleService = peopleService;
        this.booksService = booksService;
    }

    @GetMapping("admin/people")
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        return "admin/people/index";
    }
    @GetMapping("admin/people/search")
    public String searchPageAdmin(){
        return "admin/people/search";
    }

    @GetMapping("admin/people/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", booksService.findBooksById(id));
        return "admin/people/show";
    }

    @GetMapping("admin/people/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "admin/people/new";
    }

    @PostMapping("admin/people/search")
    public String search(Model model,
                              @RequestParam("name") String name) {
        model.addAttribute("people", peopleService.findByName(name));
        return "admin/people/result";
    }


    @PostMapping("admin/people")
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/people/new";

        peopleService.save(person);
        return "redirect:/admin/people";
    }

    @GetMapping("admin/people/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findOne(id));
        return "admin/people/edit";
    }


    @PatchMapping("admin/people/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/people/edit";

        peopleService.update(id, person);
        return "redirect:/admin/people";
    }

    @DeleteMapping("admin/people/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/admin/people";
    }
}

