package ru.chetvertak.TheLibrary.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.chetvertak.TheLibrary.dto.BookDTO;
import ru.chetvertak.TheLibrary.config.models.Book;

@Component
public class BookMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public BookMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Book convertToBook(BookDTO bookDTO){
        return modelMapper.map(bookDTO, Book.class);
    }

    public BookDTO convertToBookDTO(Book book){
        return modelMapper.map(book, BookDTO.class);
    }
}
