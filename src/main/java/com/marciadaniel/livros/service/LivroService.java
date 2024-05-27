package com.marciadaniel.livros.service;

import com.marciadaniel.livros.controllers.LivroController;
import com.marciadaniel.livros.dtos.LivroDto;
import com.marciadaniel.livros.models.LivroModel;
import com.marciadaniel.livros.repositories.LivroRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class LivroService {

    private final LivroRepository livroRepository;


    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public List<LivroModel> getAll() {

      return  livroRepository.findAll();

    }

    public Optional<LivroModel> getById(@PathVariable(value="id") UUID id) {
        return livroRepository.findById(id);
    }


    public LivroModel save( LivroDto livroDto) {

        var livroModel = new LivroModel();
        BeanUtils.copyProperties(livroDto, livroModel);
        livroModel.setDataRegistro(LocalDateTime.now(ZoneId.of("UTC")));

        return livroRepository.save(livroModel);
    }

    public void deleteById(UUID userId) {



        var livroExists = livroRepository.existsById(userId);

        if (livroExists) {
            livroRepository.deleteById(userId);
        }

    }


    public Object updateUserById(LivroDto livroDto) {

        var livroModel = new LivroModel();
        BeanUtils.copyProperties(livroDto, livroModel);
        livroModel.setDataRegistro(LocalDateTime.now(ZoneId.of("UTC")));

    return livroRepository.save(livroModel);
    }
}
