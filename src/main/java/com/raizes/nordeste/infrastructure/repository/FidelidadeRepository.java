package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.Fidelidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface FidelidadeRepository extends JpaRepository<Fidelidade, Long> {

    //Tenta encontrar o programa de fidelidade de um cliente especifico
    Optional<Fidelidade> findByClienteId(Long clienteId);

    //Verifica se o cliente ja tem programa de fidelidade cadastrado
    boolean existsByClienteId(Long clienteId);
}