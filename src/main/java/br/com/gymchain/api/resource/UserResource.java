package br.com.gymchain.api.resource;


import java.util.List;
import java.util.Optional;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils; // NOVO: para atualização parcial
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping; // NOVO
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping; // NOVO
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus; // NOVO
import org.springframework.web.bind.annotation.RestController;

import br.com.gymchain.api.domain.model.User;
import br.com.gymchain.api.repository.UserRepository;

// NOVO: Importações para segurança
import org.springframework.security.access.prepost.PreAuthorize; // NOVO: Para controle de acesso
import org.springframework.security.crypto.password.PasswordEncoder; // NOVO: para criptografar senha

@RestController
@RequestMapping("/users") // Mapeamento base para todos os métodos
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // NOVO: Injetar o encoder de senha

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_SEARCH_USER') and #oauth2.hasScope('read')")
    public List<User> list() {
        return userRepository.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna 201 Created
    @PreAuthorize("hasAuthority('ROLE_REGISTER_USER') and #oauth2.hasScope('write')")
    public User create(@Valid @RequestBody User user, HttpServletResponse response) {
        // Criptografar a senha antes de salvar
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_SEARCH_USER') and #oauth2.hasScope('read')")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna 204 No Content para deleção bem-sucedida
    @PreAuthorize("hasAuthority('ROLE_REMOVE_USER') and #oauth2.hasScope('write')")
    public void delete(@PathVariable Long id) {
        userRepository.deleteById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_REGISTER_USER') and #oauth2.hasScope('write')") // Registro de user para criar ou alterar
    public ResponseEntity<User> update(@PathVariable Long id, @Valid @RequestBody User user) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOptional.get();
        // Copia as propriedades do user recebido para o existingUser, ignorando o ID e a senha
        // A senha não deve ser atualizada diretamente aqui, mas por um endpoint específico de troca de senha
        BeanUtils.copyProperties(user, existingUser, "id", "password", "creationDate");

        // Se uma nova senha for fornecida no PUT, criptografe-a e atualize
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(updatedUser);
    }

    // NOVO: Endpoint para atualizar o campo 'active' do usuário
    @PutMapping("/{id}/active")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('ROLE_REGISTER_USER') and #oauth2.hasScope('write')")
    public void updateActiveProperty(@PathVariable Long id, @RequestBody Boolean active) {
        Optional<User> existingUserOptional = userRepository.findById(id);
        if (existingUserOptional.isEmpty()) {
            // Lançar uma exceção ou retornar 404
            throw new IllegalArgumentException("User not found for ID: " + id);
        }
        User existingUser = existingUserOptional.get();
        existingUser.setActive(active);
        userRepository.save(existingUser);
    }
}
