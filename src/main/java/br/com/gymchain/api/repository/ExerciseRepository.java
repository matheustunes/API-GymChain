package br.com.gymchain.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import br.com.gymchain.api.domain.model.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    // Métodos personalizados de busca, se necessário, ex: findByNameContainingIgnoreCase
}