package com.marciadaniel.livros.controllers;

import com.marciadaniel.livros.dtos.LivroDto;
import com.marciadaniel.livros.models.LivroModel;
import com.marciadaniel.livros.repositories.LivroRepository;
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

    @GetMapping("/livros")
    public ResponseEntity<List<LivroModel>> getAll(){
        List<LivroModel> livroList =
                livroRepository.findAll();
        if(!livroList.isEmpty()) {
            for(LivroModel livro : livroList) {
                UUID id = livro.getId();
                livro.add(linkTo(methodOn(LivroController.class).getOne(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(livroList);
    }

    @GetMapping("/livros/{id}")
    public ResponseEntity<Object>
    getOne(@PathVariable(value="id") UUID id){
        Optional<LivroModel> livroOptional =
                livroRepository.findById(id);
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
        var livroModel = new LivroModel();
        BeanUtils.copyProperties(livroDto, livroModel);
        livroModel.setDataRegistro(LocalDateTime.now(ZoneId.of("UTC")));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(livroRepository.save(livroModel));
    }
    @DeleteMapping("/livros/{id}")
    public ResponseEntity<Object>
    delete(@PathVariable(value="id") UUID id) {
        Optional<LivroModel> livroOptional =
                livroRepository.findById(id);
        if(livroOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Not found.");
        }
        livroRepository.delete(livroOptional.get());
        return ResponseEntity.status(HttpStatus.OK)
                .body("Deleted successfully.");
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
                .body(livroRepository.save(livroModel));
    }



}
