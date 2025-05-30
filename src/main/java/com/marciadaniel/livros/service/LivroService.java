package com.marciadaniel.livros.service;

import com.marciadaniel.livros.models.LivroModel;
import com.marciadaniel.livros.dtos.LivroDto;
import com.marciadaniel.livros.models.LivroModel;
import com.marciadaniel.livros.repositories.LivroRepository;
import com.marciadaniel.livros.requests.PaginationRequest;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

import lombok.*;
import org.springframework.data.domain.Sort;
import lombok.experimental.SuperBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.data.domain.Page;
@Service
public class LivroService {

    private final LivroRepository livroRepository;



    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    public PagingResult<LivroModel> getAll(PaginationRequest request) {
        Sort sort = Sort.by(request.getDirection(), request.getSortField());
        final Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
        final Page<LivroModel> entities = livroRepository.findAll(pageable);
        final List<LivroModel> entitiesList = entities.toList();
        return new PagingResult<>(
                entitiesList,
                entities.getTotalPages(),
                entities.getTotalElements(),
                entities.getSize(),
                entities.getNumber(),
                entities.isEmpty()
        );
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
