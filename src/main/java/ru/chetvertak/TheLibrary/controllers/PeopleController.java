package ru.chetvertak.TheLibrary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.chetvertak.TheLibrary.dto.PersonDTO;
import ru.chetvertak.TheLibrary.mapper.BookMapper;
import ru.chetvertak.TheLibrary.mapper.PersonMapper;
import ru.chetvertak.TheLibrary.config.models.Person;
import ru.chetvertak.TheLibrary.services.BooksService;
import ru.chetvertak.TheLibrary.services.PeopleService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
public class PeopleController {

    private final PeopleService peopleService;
    private final BooksService booksService;
    private final PersonMapper personMapper;
    private final BookMapper bookMapper;
    
    @Autowired
    public PeopleController(PeopleService peopleService, BooksService booksService, PersonMapper personMapper, BookMapper bookMapper) {
        this.peopleService = peopleService;
        this.booksService = booksService;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
    }

    @GetMapping("admin/people")
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll().stream().map(personMapper::convertToPersonDTO)
                .collect(Collectors.toList()));
        return "admin/people/index";
    }
    @GetMapping("admin/people/search")
    public String searchPageAdmin(){
        return "admin/people/search";
    }

    @GetMapping("admin/people/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personMapper.convertToPersonDTO(peopleService.findOne(id)));
        model.addAttribute("books", booksService.findBooksById(id).stream().map(bookMapper::convertToBookDTO)
                .collect(Collectors.toList()));
        return "admin/people/show";
    }

    @GetMapping("admin/people/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "admin/people/new";
    }

    @PostMapping("admin/people/search")
    public String search(Model model,
                              @RequestParam("name") String name) {
        model.addAttribute("people", peopleService.findByName(name).stream().map(personMapper::convertToPersonDTO)
                .collect(Collectors.toList()));
        return "admin/people/result";
    }


    @PostMapping("admin/people")
    public String create(@ModelAttribute("person") @Valid PersonDTO personDTO,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "admin/people/new";

        peopleService.save(personMapper.convertToPerson(personDTO));
        return "redirect:/admin/people";
    }

    @GetMapping("admin/people/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personMapper.convertToPersonDTO(peopleService.findOne(id)));
        return "admin/people/edit";
    }


    @PatchMapping("admin/people/{id}")
    public String update(@ModelAttribute("person") @Valid PersonDTO personDTO,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors())
            return "admin/people/edit";

        peopleService.update(id, personMapper.convertToPerson(personDTO));
        return "redirect:/admin/people";
    }

    @DeleteMapping("admin/people/{id}")
    public String delete(@PathVariable("id") int id) {
        peopleService.delete(id);
        return "redirect:/admin/people";
    }

}

