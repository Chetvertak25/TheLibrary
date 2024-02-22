package ru.chetvertak.TheLibrary.controllers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;
import ru.chetvertak.TheLibrary.mapper.BookMapper;
import ru.chetvertak.TheLibrary.mapper.PersonMapper;
import ru.chetvertak.TheLibrary.services.BooksService;
import ru.chetvertak.TheLibrary.services.PeopleService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PeopleControllerTest {

    private PeopleService peopleService = mock(PeopleService.class);
    private BooksService booksService = mock(BooksService.class);
    private PersonMapper personMapper = mock(PersonMapper.class);
    private BookMapper bookMapper = mock(BookMapper.class);

    private PeopleController peopleController = new PeopleController(peopleService, booksService, personMapper, bookMapper);

    @Test
    void testIndex() {
        Model model = mock(Model.class);

        String result = peopleController.index(model);

        verify(model).addAttribute("people", peopleService.findAll());
        assertEquals("admin/people/index", result);
    }

    @Test
    void testSearchPageAdmin() {
        String result = peopleController.searchPageAdmin();
        assertEquals("admin/people/search", result);
    }
}
