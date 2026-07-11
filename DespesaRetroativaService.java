
package com.bradesco.sharewallet.Service;

@Service
public class DespesaRetroativaService {

    private final DespesaRetroativaRepository despesaRepository;
    private final VotoDespesaRepository votoRepository;
    private final GrupoRepository grupoRepository;
    private final ParticipanteRepository participanteRepository;

    public DespesaRetroativaService(DespesaRetroativaRepository despesaRepository,
                                    VotoDespesaRepository votoRepository,
                                    GrupoRepository grupoRepository,
                                    ParticipanteRepository participanteRepository) {
        this.despesaRepository = despesaRepository;
        this.votoRepository = votoRepository;
        this.grupoRepository = grupoRepository;
        this.participanteRepository = participanteRepository;
    }


    public DespesaRetroativaResponse registrar(Long grupoId, RegistrarDespesaRetroativaRequest request) {

        Grupo grupo = grupoRepository.findById(grupoId).orElse(null);
        if (grupo == null) {
            throw new RecursoNaoEncontradoException("Grupo não encontrado");
        }

        if (grupo.getStatus() != StatusGrupo.ACERTO_DE_CONTAS) {
            throw new ConflitoDeEstadoException("Grupo não está em fase de acerto de contas");
        }


        List<Participante> participantesDoGrupo = participanteRepository.findByGrupoId(grupoId);

        List<Long> idsParticipantesDoGrupo = new ArrayList<>();
        for (Participante p : participantesDoGrupo) {
            idsParticipantesDoGrupo.add(p.getId());
        }

        for (Long idInformado : request.getParticipantesIds()) {
            if (!idsParticipantesDoGrupo.contains(idInformado)) {
                throw new ValidacaoException("Participante " + idInformado + " não pertence ao grupo");
            }
        }

        if (request.getDataHorario().isAfter(LocalDateTime.now())) {
            throw new ValidacaoException("Data/horário não pode ser futura");
        }

        List<Participante> participantesEnvolvidos = new ArrayList<>();
        for (Participante p : participantesDoGrupo) {
            if (request.getParticipantesIds().contains(p.getId())) {
                participantesEnvolvidos.add(p);
            }
        }

        DespesaRetroativa despesa = new DespesaRetroativa();
        despesa.setGrupo(grupo);
        despesa.setDescricao(request.getDescricao());
        despesa.setValor(request.getValor());
        despesa.setMeioPagamento(request.getMeioPagamento());
        despesa.setDataHorario(request.getDataHorario());
        despesa.setStatusDespesa(StatusDespesaRetroativa.PENDENTE_APROVACAO);
        despesa.setVotosNecessarios(participantesDoGrupo.size());
        despesa.setParticipantes(participantesEnvolvidos);

        DespesaRetroativa salva = despesaRepository.save(despesa);

        // Passo 5 — cria uma linha de voto (sem voto ainda) para cada participante do grupo
        List<VotoDespesaEntity> votos = new ArrayList<>();
        for (Participante p : participantesDoGrupo) {
            VotoDespesaEntity v = new VotoDespesaEntity();
            v.setDespesa(salva);
            v.setParticipante(p);
            votos.add(v);
        }
        votoRepository.saveAll(votos);

        return DespesaRetroativaResponse.fromEntity(salva, 0);
    }
}