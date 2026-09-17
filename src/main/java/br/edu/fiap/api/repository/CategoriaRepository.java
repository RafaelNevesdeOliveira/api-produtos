package br.edu.fiap.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import br.edu.fiap.api.entity.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
