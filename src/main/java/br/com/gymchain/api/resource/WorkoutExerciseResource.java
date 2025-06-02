package br.com.gymchain.api.resource;

import br.com.gymchain.api.domain.model.Exercise;
import br.com.gymchain.api.domain.model.Workout;
import br.com.gymchain.api.domain.model.WorkoutExercise;
import br.com.gymchain.api.repository.ExerciseRepository;
import br.com.gymchain.api.repository.WorkoutRepository;
import br.com.gymchain.api.repository.WorkoutExerciseRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workout-exercises")
public class WorkoutExerciseResource {

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_WORKOUT') and #oauth2.hasScope('read')") // Permissão de workout para ver detalhes
    public List<WorkoutExercise> list() {
        return workoutExerciseRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_REGISTER_WORKOUT') and #oauth2.hasScope('write')") // Permissão de workout para registrar detalhes
    public WorkoutExercise create(@Valid @RequestBody WorkoutExercise workoutExercise, HttpServletResponse response) {
        // Validar Workout
        if (workoutExercise.getWorkout() == null || workoutExercise.getWorkout().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Treino associado ao exercício é obrigatório.");
        }
        Optional<Workout> existingWorkout = workoutRepository.findById(workoutExercise.getWorkout().getId());
        if (existingWorkout.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Treino associado inexistente.");
        }
        workoutExercise.setWorkout(existingWorkout.get()); // Garante que estamos usando a instância gerenciada

        // Validar Exercise
        if (workoutExercise.getExercise() == null || workoutExercise.getExercise().getId() == null) {
            throw new  ResponseStatusException(HttpStatus.BAD_REQUEST, "Exercício da atividade é obrigatório.");
        }
        Optional<Exercise> existingExercise = exerciseRepository.findById(workoutExercise.getExercise().getId());
        if (existingExercise.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exercício da atividade inexistente.");
        }
        workoutExercise.setExercise(existingExercise.get()); // Garante que estamos usando a instância gerenciada

        return workoutExerciseRepository.save(workoutExercise);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_WORKOUT') and #oauth2.hasScope('read')")
    public ResponseEntity<WorkoutExercise> findById(@PathVariable Long id) {
        Optional<WorkoutExercise> workoutExercise = workoutExerciseRepository.findById(id);
        return workoutExercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVE_WORKOUT') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long id) {
        workoutExerciseRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REGISTER_WORKOUT') and #oauth2.hasScope('write')")
    public ResponseEntity<WorkoutExercise> update(@PathVariable Long id, @Valid @RequestBody WorkoutExercise workoutExercise) {
        Optional<WorkoutExercise> existingWorkoutExerciseOptional = workoutExerciseRepository.findById(id);
        if (existingWorkoutExerciseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Validar e setar Workout/Exercise como no método POST
        if (workoutExercise.getWorkout() == null || workoutExercise.getWorkout().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Treino associado ao exercício é obrigatório.");
        }
        Optional<Workout> existingWorkout = workoutRepository.findById(workoutExercise.getWorkout().getId());
        if (existingWorkout.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Treino associado inexistente.");
        }
        workoutExercise.setWorkout(existingWorkout.get());

        if (workoutExercise.getExercise() == null || workoutExercise.getExercise().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exercício da atividade é obrigatório.");
        }
        Optional<Exercise> existingExercise = exerciseRepository.findById(workoutExercise.getExercise().getId());
        if (existingExercise.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Exercício da atividade inexistente.");
        }
        workoutExercise.setExercise(existingExercise.get());

        WorkoutExercise existingWorkoutExercise = existingWorkoutExerciseOptional.get();
        BeanUtils.copyProperties(workoutExercise, existingWorkoutExercise, "id");
        return ResponseEntity.ok(workoutExerciseRepository.save(existingWorkoutExercise));
    }
}
