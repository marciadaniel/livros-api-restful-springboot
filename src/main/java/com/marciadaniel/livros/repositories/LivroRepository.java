package com.marciadaniel.livros.repositories;

import com.marciadaniel.livros.models.LivroModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface LivroRepository extends JpaRepository<LivroModel, UUID> {

}
