package br.com.gymchain.api.security;

import br.com.gymchain.api.domain.model.User;
import br.com.gymchain.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .authorities(
                user.getPermissions()   // pega a lista de Permission
                    .stream()
                    .map(p -> p.getDescription().toUpperCase()) 
                    // supondo que o campo seja 'description' em Permission,
                    // e usamos uppercase apenas por convenção de ROLE_…
                    .toArray(String[]::new)
            )
            .build();
    }
}
