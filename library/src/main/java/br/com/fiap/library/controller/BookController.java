package br.com.fiap.library.controller;


import br.com.fiap.library.dto.AutorDTO;
import br.com.fiap.library.dto.BookDTO;
import br.com.fiap.library.dto.CreateBookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("books")
public class BookController {
    List<BookDTO> bookDTOSList = new ArrayList<>();

    @GetMapping
    public List<BookDTO> GetAll(@RequestParam(required = false, value = "title") String titulo) {

        bookDTOSList.add(new BookDTO(1,
                "Senhor dos Aneis",
                800,
                "213221212", ZonedDateTime.now().minusYears(40),
                new AutorDTO()));

        bookDTOSList.add(new BookDTO(2,
                "Senhor dos Aneis 2",
                800,
                "11111111", ZonedDateTime.now().minusYears(40),
                new AutorDTO()));

        return bookDTOSList.stream()
                .filter(bookDTO -> titulo == null || bookDTO.getTitulo().startsWith(titulo))
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public BookDTO findById(@PathVariable Integer id) {
        return bookDTOSList.stream()
                .filter(bookDTO -> bookDTO.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public BookController(List<BookDTO> bookDTOSList) {
        this.bookDTOSList = bookDTOSList;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookDTO create(@RequestBody CreateBookDTO createBookDTO) {
        BookDTO bookDTO = new BookDTO(createBookDTO, bookDTOSList.size() + 1);
        bookDTOSList.add(bookDTO);
        return bookDTO;
    }

    @PutMapping("{id}")
    public BookDTO update(@PathVariable Integer id,
                          @RequestBody CreateBookDTO createBookDTO){
    BookDTO bookDTO = findById(id);
    bookDTO.setDataLancamento(createBookDTO.getDataLancamento());
    bookDTO.setISBN(createBookDTO.getISBN());
    bookDTO.setQuantidadeDePaginas(createBookDTO.getQuantidadeDePaginas());
    bookDTO.setTitulo(createBookDTO.getTitulo());

    return bookDTO;
    }

    @PatchMapping("{id}")
    public BookDTO update(@PathVariable Integer id,
                          @RequestBody String titulo){
        BookDTO bookDTO = findById(id);
        bookDTO.setTitulo(titulo);
        return bookDTO;
    }

    @DeleteMapping("{id}")
    public void delete (@PathVariable Integer id){
        BookDTO bookDTO = findById(id);
        bookDTOSList.remove(bookDTO);
    }
}
