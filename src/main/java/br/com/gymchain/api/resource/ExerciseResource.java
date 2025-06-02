package br.com.gymchain.api.resource;



import br.com.gymchain.api.domain.model.Exercise;
import br.com.gymchain.api.repository.ExerciseRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/exercises")
public class ExerciseResource {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_EXERCISE') and #oauth2.hasScope('read')")
    public List<Exercise> list() {
        return exerciseRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_REGISTER_EXERCISE') and #oauth2.hasScope('write')")
    public Exercise create(@Valid @RequestBody Exercise exercise, HttpServletResponse response) {
        return exerciseRepository.save(exercise);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_EXERCISE') and #oauth2.hasScope('read')")
    public ResponseEntity<Exercise> findById(@PathVariable Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        return exercise.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVE_EXERCISE') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long id) {
        exerciseRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REGISTER_EXERCISE') and #oauth2.hasScope('write')")
    public ResponseEntity<Exercise> update(@PathVariable Long id, @Valid @RequestBody Exercise exercise) {
        Optional<Exercise> existingExerciseOptional = exerciseRepository.findById(id);
        if (existingExerciseOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Exercise existingExercise = existingExerciseOptional.get();
        BeanUtils.copyProperties(exercise, existingExercise, "id"); // Copia propriedades, ignorando o ID
        return ResponseEntity.ok(exerciseRepository.save(existingExercise));
    }
}