package com.raizes.nordeste.infrastructure.security;


import com.raizes.nordeste.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



//chama esse metodo durante o login com o valor que o usuario digitou no campo de identificacao
@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return usuarioRepository.findByEmailAndAtivoTrue(email)
                .map(usuario -> User.builder()
                        .username(usuario.getEmail())
                        .password(usuario.getSenhaHash())
                        .roles(usuario.getPerfil().name())
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario nao encontrado: " + email));
    }
}