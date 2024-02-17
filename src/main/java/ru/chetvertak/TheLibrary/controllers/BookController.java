package ru.chetvertak.TheLibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.chetvertak.TheLibrary.models.Book;

import javax.validation.Valid;

import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.services.BooksService;
import ru.chetvertak.TheLibrary.services.PeopleService;

import java.util.Optional;

@Controller
public class BookController {

    private final BooksService booksService;
    private final PeopleService peopleService;

    @Autowired
    public BookController(BooksService booksService, PeopleService peopleService) {
        this.booksService = booksService;
        this.peopleService = peopleService;
    }

    @GetMapping("admin/books")
    public String indexAdmin(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "books_per_page", required = false, defaultValue = "0") int booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") boolean sort,
                        Model model) {

        model.addAttribute("books", booksService.getAllBooks(page, booksPerPage, sort));
        return "admin/books/index";
    }

    @PreAuthorize("hasRole(USER)")
    @GetMapping("user/books")
    public String indexUser(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                        @RequestParam(value = "books_per_page", required = false, defaultValue = "0") int booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false, defaultValue = "false") boolean sort,
                        Model model) {

        model.addAttribute("books", booksService.getAllBooks(page, booksPerPage, sort));
        return "user/books/index";
    }

    @GetMapping("admin/books/{id}")
    public String showBookPageAdmin(@PathVariable("id") int id, Model model,
                       @ModelAttribute("person") Person person) {
        model.addAttribute("book", booksService.findOne(id));
        Person owner = booksService.findOne(id).getOwner();
        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", peopleService.findAll());
        }
        return "admin/books/show";
    }

    @PreAuthorize("hasRole(USER)")
    @GetMapping("user/books/{id}")
    private String showBookPageUser(@PathVariable("id") int id,
                                    Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Person> person = peopleService.findByUsername(authentication.getName());
        model.addAttribute("person", person.get());
        model.addAttribute("book", booksService.findOne(id));
        return "user/books/show";
    }

    @GetMapping("admin/books/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "admin/books/new";
    }

    @PostMapping("admin/books")
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/books/new";

        booksService.save(book);
        return "redirect:/admin/books";
    }

    @GetMapping("admin/books/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findOne(id));
        return "admin/books/edit";
    }

    @GetMapping("admin/books/search")
    public String searchPageAdmin(){
        return "admin/books/search";
    }

    @PreAuthorize("hasRole(USER)")
    @GetMapping("user/books/search")
    public String searchPageUser(){
        return "user/books/search";
    }

    @PreAuthorize("hasRole(USER)")
    @GetMapping("user/books/my")
    public String myBooksPageUser(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Person> person = peopleService.findByUsername(authentication.getName());
        model.addAttribute("person", person.get());
        return "user/books/my";
    }


    @GetMapping("admin/books/my")
    public String myBooksPageAdmin(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<Person> person = peopleService.findByUsername(authentication.getName());
        model.addAttribute("person", person.get());
        return "admin/books/my";
    }

    @PostMapping("admin/books/search")
    public String searchAdmin(Model model,
                         @RequestParam("title") String title) {
        model.addAttribute("books", booksService.searchByTitle(title));
        return "admin/books/result";
    }

    @PreAuthorize("hasRole(USER)")
    @PostMapping("user/books/search")
    public String searchUser(Model model,
                         @RequestParam("title") String title) {
        model.addAttribute("books", booksService.searchByTitle(title));
        return "user/books/result";
    }


        @PatchMapping("admin/books/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/books/edit";

        booksService.update(id, book);
        return "redirect:/admin/books";
    }

    @DeleteMapping("admin/books/{id}")
    public String delete(@PathVariable("id") int id) {
        booksService.delete(id);
        return "redirect:/admin/books";
    }


    @DeleteMapping("admin/books/clear/{id}")
    public String deleteAllBooks(@PathVariable("id") int id) {
        booksService.releaseAllOwnerToBook(id);
        return "redirect:/admin/books/my";
    }

    @PreAuthorize("hasRole(USER)")
    @DeleteMapping("user/books/clear/{id}")
    public String deleteAllBooksUser(@PathVariable("id") int id) {
        booksService.releaseAllOwnerToBook(id);
        return "redirect:/user/books/my";
    }


    @PatchMapping("admin/books/{id}/assign")
    public String assignAdmin(@PathVariable("id") int id,
                         @ModelAttribute("person") Person person) {
        booksService.assignOwnerToBook(id, person);
        return "redirect:/admin/books/" + id;
    }
    @PreAuthorize("hasRole(USER)")
    @PatchMapping("user/books/{id}/assign")
    public String assignUser(@PathVariable("id") int id,
                         @ModelAttribute("person") Person person) {
        booksService.assignOwnerToBook(id, person);
        return "redirect:/user/books/" + id;
    }


    @PatchMapping("admin/books/{id}/release")
    public String releaseAdmin(@PathVariable("id") int id) {
        booksService.releaseOwnerToBook(id);

        return "redirect:/admin/books/" + id;
    }
    @PreAuthorize("hasRole(USER)")
    @PatchMapping("user/books/{id}/release")
    public String releaseUser(@PathVariable("id") int id) {
        booksService.releaseOwnerToBook(id);

        return "redirect:/user/books/" + id;
    }


}
