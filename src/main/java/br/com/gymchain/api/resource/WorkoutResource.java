package br.com.gymchain.api.resource;

import br.com.gymchain.api.domain.model.Workout;
import br.com.gymchain.api.domain.model.User; // Import para validação do usuário
import br.com.gymchain.api.repository.WorkoutRepository;
import br.com.gymchain.api.repository.UserRepository; // Import para validação do usuário
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // NOVO: para tratamento de exceção

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workouts")
public class WorkoutResource {

    @Autowired
    private WorkoutRepository workoutRepository;

    @Autowired
    private UserRepository userRepository; // Para validar se o usuário existe e está ativo

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_WORKOUT') and #oauth2.hasScope('read')")
    public List<Workout> list() {
        return workoutRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('ROLE_REGISTER_WORKOUT') and #oauth2.hasScope('write')")
    public Workout create(@Valid @RequestBody Workout workout, HttpServletResponse response) {
        // **Validação de usuário ativo/existente antes de salvar o treino**
        User user = workout.getUser();
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário do treino é obrigatório.");
        }
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty() || !existingUser.get().getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente ou inativo para o treino.");
        }
        // A lógica de serviço para salvar o treino e talvez calcular HP seria aqui
        workout.setTotalHpEarned(calculateHpForWorkout(workout)); // Exemplo de lógica gamificada
        return workoutRepository.save(workout);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_WORKOUT') and #oauth2.hasScope('read')")
    public ResponseEntity<Workout> findById(@PathVariable Long id) {
        Optional<Workout> workout = workoutRepository.findById(id);
        return workout.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REMOVE_WORKOUT') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long id) {
        workoutRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REGISTER_WORKOUT') and #oauth2.hasScope('write')")
    public ResponseEntity<Workout> update(@PathVariable Long id, @Valid @RequestBody Workout workout) {
        Optional<Workout> existingWorkoutOptional = workoutRepository.findById(id);
        if (existingWorkoutOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Validação de usuário ativo/existente antes de atualizar o treino
        User user = workout.getUser();
        if (user == null || user.getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário do treino é obrigatório.");
        }
        Optional<User> existingUser = userRepository.findById(user.getId());
        if (existingUser.isEmpty() || !existingUser.get().getActive()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário inexistente ou inativo para o treino.");
        }

        Workout existingWorkout = existingWorkoutOptional.get();
        BeanUtils.copyProperties(workout, existingWorkout, "id", "totalHpEarned"); // totalHpEarned pode ser recalculado ou não atualizado diretamente

        // Recalcular HP se a lógica de treino for alterada significativamente
        existingWorkout.setTotalHpEarned(calculateHpForWorkout(existingWorkout));

        return ResponseEntity.ok(workoutRepository.save(existingWorkout));
    }

    // Método de exemplo para calcular HP (pode ser mais complexo)
    private Integer calculateHpForWorkout(Workout workout) {
        // Lógica simples: 10 HP por cada 10 minutos de treino
        return (workout.getDurationMinutes() != null ? workout.getDurationMinutes() / 10 : 0) * 10;
    }
}
