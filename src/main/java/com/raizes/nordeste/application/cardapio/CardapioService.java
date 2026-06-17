package com.raizes.nordeste.application.cardapio;


import com.raizes.nordeste.api.dto.response.CardapioResponse;
import com.raizes.nordeste.api.exception.RecursoNaoEncontradoException;
import com.raizes.nordeste.infrastructure.repository.CardapioUnidadeRepository;
import com.raizes.nordeste.infrastructure.repository.UnidadeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CardapioService {

    private final CardapioUnidadeRepository cardapioUnidadeRepository;
    private final UnidadeRepository unidadeRepository;


    @Transactional(readOnly = true)
    public List<CardapioResponse> consultarPorUnidade(Long unidadeId) {

        if (!unidadeRepository.existsById(unidadeId)) {
            throw new RecursoNaoEncontradoException("Unidade", unidadeId);
        }


        return cardapioUnidadeRepository
                .findByUnidadeIdAndDisponivelTrue(unidadeId)
                 .stream()
                .map(CardapioResponse::from)
                .toList();
    }
}