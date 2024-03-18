package ru.chetvertak.TheLibrary.controllers;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import ru.chetvertak.TheLibrary.dto.BookDTO;
import ru.chetvertak.TheLibrary.dto.PersonDTO;
import ru.chetvertak.TheLibrary.mapper.BookMapper;
import ru.chetvertak.TheLibrary.mapper.PersonMapper;
import ru.chetvertak.TheLibrary.config.models.Book;
import ru.chetvertak.TheLibrary.config.models.Person;
import ru.chetvertak.TheLibrary.config.models.Role;
import ru.chetvertak.TheLibrary.services.BooksService;
import ru.chetvertak.TheLibrary.services.PeopleService;

@ContextConfiguration(classes = {BookController.class})
@ExtendWith(SpringExtension.class)
class BookControllerTest {
    @Autowired
    private BookController bookController;

    @MockBean
    private BookMapper bookMapper;

    @MockBean
    private BooksService booksService;

    @MockBean
    private PeopleService peopleService;

    @MockBean
    private PersonMapper personMapper;


    @Test
    void testUpdate() throws Exception {
        when(booksService.update(anyInt(), (Book) any())).thenReturn(true);

        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);

        Book book = new Book();
        book.setAuthor("testAuthor");
        book.setId(1);
        book.setOverdue(true);
        book.setOwner(person);
        book.setTakeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        book.setTitle("testName");
        book.setYear(1);
        when(bookMapper.convertToBook((BookDTO) any())).thenReturn(book);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/admin/books/{id}", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/books"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/books"));
    }


    @Test
    void testAssignAdmin() throws Exception {
        when(booksService.assignOwnerToBook(anyInt(), (Person) any())).thenReturn(true);

        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);
        when(personMapper.convertToPerson((PersonDTO) any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/admin/books/{id}/assign", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/books/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/books/1"));
    }


    @Test
    void testDelete() throws Exception {
        when(booksService.delete(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/books/{id}", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/books"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/books"));
    }


    @Test
    void testDeleteAllBooks() throws Exception {
        when(booksService.releaseAllOwnerToBook(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/admin/books/clear/{id}", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/books/my"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/books/my"));
    }


    @Test
    void testDeleteAllBooksUser() throws Exception {
        when(booksService.releaseAllOwnerToBook(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/user/books/clear/{id}", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/books/my"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/books/my"));
    }


    @Test
    void testAssignUser() throws Exception {
        when(booksService.assignOwnerToBook(anyInt(), (Person) any())).thenReturn(true);

        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);
        when(personMapper.convertToPerson((PersonDTO) any())).thenReturn(person);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.patch("/user/books/{id}/assign", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("person"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/books/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/books/1"));
    }


    @Test
    void testCreate() throws Exception {
        when(booksService.save((Book) any())).thenReturn(true);

        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);

        Book book = new Book();
        book.setAuthor("JaneDoe");
        book.setId(1);
        book.setOverdue(true);
        book.setOwner(person);
        book.setTakeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        book.setTitle("Name");
        book.setYear(1);
        when(bookMapper.convertToBook((BookDTO) any())).thenReturn(book);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/books");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/books"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/books"));
    }

    /**
     * Method under test: {@link BookController#edit(Model, int)}
     */
    @Test
    void testEdit() throws Exception {
        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("Name");
        person.setPassword("iloveyou");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("janedoe");
        person.setYearOfBirth(1);

        Book book = new Book();
        book.setAuthor("JaneDoe");
        book.setId(1);
        book.setOverdue(true);
        book.setOwner(person);
        book.setTakeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        book.setTitle("Name");
        book.setYear(1);
        when(booksService.findOne(anyInt())).thenReturn(book);

        Person person1 = new Person();
        person1.setBooks(new ArrayList<>());
        person1.setId(1);
        person1.setName("Name");
        person1.setPassword("iloveyou");
        person1.setRole(Role.ROLE_ADMIN);
        person1.setUsername("janedoe");
        person1.setYearOfBirth(1);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("JaneDoe");
        bookDTO.setId(1);
        bookDTO.setOverdue(true);
        bookDTO.setOwner(person1);
        bookDTO.setTitle("Dr");
        bookDTO.setYear(1);
        when(bookMapper.convertToBookDTO((Book) any())).thenReturn(bookDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/books/{id}/edit", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/edit"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/edit"));
    }


    @Test
    void testIndexAdmin() throws Exception {
        when(booksService.getAllBooks(anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/books");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/index"));
    }


    @Test
    void testIndexAdmin2() throws Exception {
        when(booksService.getAllBooks(anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/admin/books");
        getResult.characterEncoding("Encoding");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(getResult)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/index"));
    }


    @Test
    void testIndexUser() throws Exception {
        when(booksService.getAllBooks(anyInt(), anyInt(), anyBoolean())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/books");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.view().name("user/books/index"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("user/books/index"));
    }


    @Test
    void testMyBooksPageAdmin() throws Exception {
        SecurityMockMvcRequestBuilders.FormLoginRequestBuilder requestBuilder = SecurityMockMvcRequestBuilders
                .formLogin();
        ResultActions actualPerformResult = MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder);
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }


    @Test
    void testNewBook() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/books/new");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("book"))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/new"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/new"));
    }


    @Test
    void testReleaseAdmin() throws Exception {
        when(booksService.releaseOwnerToBook(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(String.join("", "/admin/books/{id}/", System.getProperty("jdk.debug")), 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/admin/books/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/admin/books/1"));
    }


    @Test
    void testReleaseUser() throws Exception {
        when(booksService.releaseOwnerToBook(anyInt())).thenReturn(true);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .patch(String.join("", "/user/books/{id}/", System.getProperty("jdk.debug")), 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isFound())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("redirect:/user/books/1"))
                .andExpect(MockMvcResultMatchers.redirectedUrl("/user/books/1"));
    }


    @Test
    void testSearchAdmin() throws Exception {
        when(booksService.searchByTitle((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/admin/books/search")
                .param("title", "foo");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/result"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/result"));
    }


    @Test
    void testSearchPageAdmin() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/books/search");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/search"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/search"));
    }


    @Test
    void testSearchPageUser() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/user/books/search");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(0))
                .andExpect(MockMvcResultMatchers.view().name("user/books/search"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("user/books/search"));
    }


    @Test
    void testSearchUser() throws Exception {
        when(booksService.searchByTitle((String) any())).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/user/books/search")
                .param("title", "foo");
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(1))
                .andExpect(MockMvcResultMatchers.model().attributeExists("books"))
                .andExpect(MockMvcResultMatchers.view().name("user/books/result"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("user/books/result"));
    }


    @Test
    void testShowBookPageAdmin() throws Exception {
        Person person = new Person();
        person.setBooks(new ArrayList<>());
        person.setId(1);
        person.setName("testName");
        person.setPassword("testPassword");
        person.setRole(Role.ROLE_ADMIN);
        person.setUsername("testUsername");
        person.setYearOfBirth(1);

        Book book = new Book();
        book.setAuthor("testAuthor");
        book.setId(1);
        book.setOverdue(true);
        book.setOwner(person);
        book.setTakeTime(LocalDateTime.of(1, 1, 1, 1, 1));
        book.setTitle("testTitle");
        book.setYear(1);
        when(booksService.findOne(anyInt())).thenReturn(book);

        PersonDTO personDTO = new PersonDTO();
        personDTO.setBooks(new ArrayList<>());
        personDTO.setId(1);
        personDTO.setName("testName");
        personDTO.setRole(Role.ROLE_ADMIN);
        personDTO.setUsername("testUsername");
        personDTO.setYearOfBirth(1);
        when(personMapper.convertToPersonDTO((Person) any())).thenReturn(personDTO);

        Person person2 = new Person();
        person2.setBooks(new ArrayList<>());
        person2.setId(1);
        person2.setName("testName2");
        person2.setPassword("testPassword2");
        person2.setRole(Role.ROLE_ADMIN);
        person2.setUsername("testUsername2");
        person2.setYearOfBirth(1);

        BookDTO bookDTO = new BookDTO();
        bookDTO.setAuthor("JaneDoe");
        bookDTO.setId(1);
        bookDTO.setOverdue(true);
        bookDTO.setOwner(person2);
        bookDTO.setTitle("Dr");
        bookDTO.setYear(1);
        when(bookMapper.convertToBookDTO((Book) any())).thenReturn(bookDTO);
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/admin/books/{id}", 1);
        MockMvcBuilders.standaloneSetup(bookController)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().size(3))
                .andExpect(MockMvcResultMatchers.model().attributeExists("book", "owner", "person"))
                .andExpect(MockMvcResultMatchers.view().name("admin/books/show"))
                .andExpect(MockMvcResultMatchers.forwardedUrl("admin/books/show"));
    }
}

