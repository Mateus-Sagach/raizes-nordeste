package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Busca usuario por email apenas se estiver ativo
    // Spring vai gerar SELECT * FROM usuarios WHERE email = ? AND ativo = true
    Optional<Usuario> findByEmailAndAtivoTrue(String email);

    // Verifica se ja existe usuario com esse email
    //Spring vai gerar SELECT COUNT(*) > 0 FROM usuarios WHERE email = ?
    boolean existsByEmail(String email);
}