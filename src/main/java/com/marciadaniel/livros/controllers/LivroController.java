package com.marciadaniel.livros.controllers;
import java.util.Map;
import java.util.HashMap;
import com.marciadaniel.livros.dtos.LivroDto;
import com.marciadaniel.livros.models.LivroModel;
import com.marciadaniel.livros.repositories.LivroRepository;
import com.marciadaniel.livros.service.LivroService;
import com.marciadaniel.livros.service.PagingResult;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import com.marciadaniel.livros.requests.PaginationRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.lang.Integer;


@RestController
public class LivroController {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    LivroService livroService;

    @GetMapping("/")
    public String home() {
        return "Bem-vindo à API de livros!";
    }

    @GetMapping("/livros")
    public ResponseEntity<Map<String, Object>> getAll(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "ASC") Sort.Direction direction
    ){


        final PaginationRequest request = new PaginationRequest(page, size, sortField, direction);
       final PagingResult<LivroModel> livroPage = livroService.getAll(request);
        List<LivroModel> livroList = new ArrayList<>(livroPage.getContent());

            if(!livroList.isEmpty()) {
                for(LivroModel livro : livroList) {
                    UUID id = livro.getId();
                    livro.add(linkTo(methodOn(LivroController.class).getById(id)).withSelfRel());
                }


        }

        Map<String, Object> response = new HashMap<>();
        response.put("content", livroList);
        response.put("page", livroPage.getPage());
        response.put("size", livroPage.getSize());
        response.put("totalElements", livroPage.getTotalElements());
        response.put("totalPages", livroPage.getTotalPages());

        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }

    @GetMapping("/livros/{id}")
    public ResponseEntity<Object>
    getById(@PathVariable(value="id") UUID id){
        Optional<LivroModel> livroOptional =
                livroService.getById(id);

        if(livroOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("not found.");
        }
        livroOptional.get().add(
                linkTo(methodOn(LivroController.class)
                        .getAll(null, null, null, null))
                        .withRel("Livros List")
        );
        return ResponseEntity.status(HttpStatus.OK)
                .body(livroOptional.get());
    }

    @PostMapping("/livros")
    public ResponseEntity<LivroModel>
    save(@RequestBody @Valid LivroDto livroDto) {
        LivroModel livro= livroService.save(livroDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(livro);
    }
    @DeleteMapping("/livros/{id}")
    public ResponseEntity<Object>
    delete(@PathVariable(value="id") UUID id) {

       livroService.deleteById(id);
        return ResponseEntity.noContent().build();


    }
    @PutMapping("/livros/{id}")
    public ResponseEntity<Object>
    update(@PathVariable(value="id") UUID id,
                  @RequestBody @Valid LivroDto livroDto) {
        Optional<LivroModel> livroOptional =
                livroRepository.findById(id);
        if(livroOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Not found.");
        }
        var livroModel = livroOptional.get();
        BeanUtils.copyProperties(livroDto,
                livroModel);
        livroModel.setDataRegistro(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.OK)
                .body( livroService.updateUserById(livroDto));
    }



}
