package br.com.gymchain.api.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import br.com.gymchain.api.domain.model.WorkoutExercise;

public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
}