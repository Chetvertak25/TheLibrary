package ru.chetvertak.TheLibrary.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.chetvertak.TheLibrary.dto.PersonDTO;
import ru.chetvertak.TheLibrary.mapper.BookMapper;
import ru.chetvertak.TheLibrary.mapper.PersonMapper;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.models.Role;
import ru.chetvertak.TheLibrary.services.BooksService;
import ru.chetvertak.TheLibrary.services.PeopleService;

@ContextConfiguration(classes = {PeopleController.class})
@ExtendWith(SpringExtension.class)
class PeopleControllerTest {
    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BooksService booksService;

    @Autowired
    private PeopleController peopleController;

    @MockBean
    private PeopleService peopleService;

    @MockBean
    private PersonMapper personMapper;


    @Test
    void testUpdate() throws Exception {
        when(peopleService.update(anyInt(), (Person) any())).thenReturn(true);

        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);
        when(personMapper.convertToPerson((PersonDTO) any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/admin/people/{id}", 1);
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/people"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/people"));
    }


    @Test
    void testCreate() throws Exception {
        when(peopleService.save((Person) any())).thenReturn(true);

        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);
        when(personMapper.convertToPerson((PersonDTO) any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/people");
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/people"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/people"));
    }


    @Test
    void testDelete() throws Exception {
        when(peopleService.delete(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/people/{id}", 1);
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/people"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/people"));
    }


    @Test
    void testEdit() throws Exception {
        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setBooks(new ArrayList<>());
        personDTO.setId(1);
        personDTO.setName("testName");
        personDTO.setRole(Role.ROLE_ADMIN);
        personDTO.setUsername("testUsername");
        personDTO.setYearOfBirth(1);
        when(personMapper.convertToPersonDTO((Person) any())).thenReturn(personDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/people/{id}/edit", 1);
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("admin/people/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/people/edit"));
    }


    @Test
    void testIndex() throws Exception {
        when(peopleService.findAll()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/people");
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("people"))
                .andExpect(MockMvcResultMatchers.view().name("admin/people/index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/people/index"));
    }



    @Test
    void testNewPerson() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/people/new");
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("admin/people/new"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/people/new"));
    }


    @Test
    void testSearch() throws Exception {
        when(peopleService.findByName((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/people/search")
                .param("name", "foo");
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("people"))
                .andExpect(MockMvcResultMatchers.view().name("admin/people/result"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/people/result"));
    }


    @Test
    void testSearchPageAdmin() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/people/search");
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("admin/people/search"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/people/search"));
    }


    @Test
    void testShow() throws Exception {
        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);
        when(peopleService.findOne(anyInt())).thenReturn(person);
        when(booksService.findBooksById(anyInt())).thenReturn(new ArrayList<>());

        PersonDTO personDTO = new PersonDTO();
        personDTO.setBooks(new ArrayList<>());
        personDTO.setId(1);
        personDTO.setName("testName");
        personDTO.setRole(Role.ROLE_ADMIN);
        personDTO.setUsername("testUsername");
        personDTO.setYearOfBirth(1);
        when(personMapper.convertToPersonDTO((Person) any())).thenReturn(personDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/people/{id}", 1);
        MockMvcBuilders.standaloneSetup(peopleController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(2))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books", "person"))
                .andExpect(MockMvcResultMatchers.view().name("admin/people/show"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/people/show"));
    }
}

