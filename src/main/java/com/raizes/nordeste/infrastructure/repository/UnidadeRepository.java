package com.raizes.nordeste.infrastructure.repository;


import com.raizes.nordeste.domain.model.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UnidadeRepository extends JpaRepository<Unidade, Long> {

    //Busca todas as unidades ativas
    // Spring gera: SELECT * FROM unidades WHERE ativo = true
    List<Unidade> findByAtivoTrue();

    // Busca unidades ativas de uma cidade especificada
    //Spring gera: SELECT * FROM unidades WHERE cidade = ? AND ativo = true
    List<Unidade> findByCidadeAndAtivoTrue(String cidade);

    // Busca unidade ativa pelo id
    //Usado para garantir que nao vai buscar unidades desativadas
    Optional<Unidade> findByIdAndAtivoTrue(Long id);
}