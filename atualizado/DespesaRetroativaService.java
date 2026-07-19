package com.bradesco.sharewallet.Services;

import com.bradesco.sharewallet.Entities.*;
import com.bradesco.sharewallet.Exceptions.*;
import com.bradesco.sharewallet.Repositories.DespesaRetroativaRepository;
import com.bradesco.sharewallet.Repositories.VotoDespesaRepository;
import com.bradesco.sharewallet.dto.VotarDespesaRequest;
import com.bradesco.sharewallet.dto.VotoDespesaResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class VotoDespesaService {

    private final VotoDespesaRepository votoRepository;
    private final DespesaRetroativaRepository despesaRepository;

    public VotoDespesaService(VotoDespesaRepository votoRepository,
                               DespesaRetroativaRepository despesaRepository) {
        this.votoRepository = votoRepository;
        this.despesaRepository = despesaRepository;
    }

    @Transactional
    public VotoDespesaResponse votar(Long despesaId, Long participanteId, VotarDespesaRequest request) {

        DespesaRetroativa despesa = despesaRepository.findById(despesaId).orElse(null);
        if (despesa == null) {
            throw new RecursoNaoEncontradoException("Despesa não encontrada");
        }

        if (despesa.getStatusDespesa() != StatusDespesaRetroativa.PENDENTE_APROVACAO) {
            throw new ConflitoDeEstadoException("Despesa não está pendente de aprovação");
        }

        Optional<VotoDespesaEntity> votoOpt =
            votoRepository.findByDespesaIdAndParticipanteId(despesaId, participanteId);

        if (votoOpt.isEmpty()) {
            throw new AcessoNegadoException("Usuário não é participante do grupo");
        }

        VotoDespesaEntity votoEntity = votoOpt.get();

        if (votoEntity.getVoto() != null) {
            throw new ValidacaoException("Usuário já votou nesta despesa");
        }

        votoEntity.setVoto(request.getVoto());
        votoEntity.setDataVoto(LocalDateTime.now());
        votoRepository.save(votoEntity);

        long votosAprovar = votoRepository.countByDespesaIdAndVoto(despesaId, VotoDespesa.APROVAR);
        long votosRejeitar = votoRepository.countByDespesaIdAndVoto(despesaId, VotoDespesa.REJEITAR);

        boolean recalculou = false;

        long votosNecessarios = despesa.getVotosNecessarios();
        long maioriaNecessaria = (votosNecessarios + 1) / 2;

        if (votosAprovar >= maioriaNecessaria) {
            despesa.setStatusDespesa(StatusDespesaRetroativa.APROVADA);
            despesaRepository.save(despesa);
            recalculou = true;

        } else if (votosRejeitar >= maioriaNecessaria) {
            despesa.setStatusDespesa(StatusDespesaRetroativa.REJEITADA);
            despesaRepository.save(despesa);
        }

        return new VotoDespesaResponse(despesaId, despesa.getStatusDespesa(), votosAprovar, votosRejeitar, recalculou);
    }

    public VotoDespesaResponse consultarVotos(Long despesaId) {
        DespesaRetroativa despesa = despesaRepository.findById(despesaId).orElse(null);
        if (despesa == null) {
            throw new RecursoNaoEncontradoException("Despesa não encontrada");
        }

        long votosAprovar = votoRepository.countByDespesaIdAndVoto(despesaId, VotoDespesa.APROVAR);
        long votosRejeitar = votoRepository.countByDespesaIdAndVoto(despesaId, VotoDespesa.REJEITAR);

        return new VotoDespesaResponse(despesaId, despesa.getStatusDespesa(), votosAprovar, votosRejeitar, false);
    }
}
