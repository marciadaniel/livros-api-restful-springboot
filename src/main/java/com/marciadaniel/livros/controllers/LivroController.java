package com.marciadaniel.livros.controllers;

import com.marciadaniel.livros.dtos.LivroDto;
import com.marciadaniel.livros.models.LivroModel;
import com.marciadaniel.livros.repositories.LivroRepository;
import com.marciadaniel.livros.service.LivroService;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class LivroController {

    @Autowired
    LivroRepository livroRepository;

    @Autowired
    LivroService livroService;

    @GetMapping("/livros")
    public ResponseEntity<List<LivroModel>> getAll(){

       List<LivroModel> livroList = livroService.getAll();
        if(!livroList.isEmpty()) {
            for(LivroModel livro : livroList) {
                UUID id = livro.getId();
                livro.add(linkTo(methodOn(LivroController.class).getById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(livroList);
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
        livroOptional.get().add(linkTo(methodOn(LivroController.class)
                .getAll()).withRel("Livros List"));
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
