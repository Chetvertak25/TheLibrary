package ru.chetvertak.TheLibrary.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import ru.chetvertak.TheLibrary.models.Person;
import ru.chetvertak.TheLibrary.services.RegistrationService;
import ru.chetvertak.TheLibrary.util.PersonValidator;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private PersonValidator personValidator;

    @Mock
    private RegistrationService registrationService;


    @Test
    void testLoginPage() {
        AuthController authController = new AuthController(personValidator, registrationService);

        String result = authController.loginPage();

        assertEquals("auth/login", result);
    }

    @Test
    void testRegistrationPage() {
        AuthController authController = new AuthController(personValidator, registrationService);
        Person person = new Person();

        String result = authController.registrationPage(person);

        assertEquals("auth/registration", result);
    }

    @Test
    void testLogout() {
        AuthController authController = new AuthController(personValidator, registrationService);
        HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
        Authentication authentication = mock(Authentication.class);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String result = authController.logout(request);

        verify(request.getSession()).invalidate();
        assertEquals("redirect:/", result);
    }


    @Test
    void testPerformRegistration() {
        Person person = new Person();
        BindingResult bindingResult = mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(false);

        AuthController authController = new AuthController(personValidator, registrationService);
        String result = authController.performRegistration(person, bindingResult);

        verify(personValidator).validate(person, bindingResult);
        verify(registrationService).register(person);
        assertEquals("redirect:/auth/login", result);
    }
}