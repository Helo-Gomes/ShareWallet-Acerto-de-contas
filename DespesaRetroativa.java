package com.bradesco.sharewallet.Entities;


import com.bradesco.sharewallet.Enums.StatusDespesaRetroativa;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "despesa_retroativa")
public class DespesaRetroativa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_despesa")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_grupo", nullable = false)
    private Grupo grupo;

    @Column(nullable = false, length = 150)
    private String descricao;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(name = "meio_pagamento", nullable = false)
    private MeioPagamento meioPagamento;

    @Column(name = "data_horario", nullable = false)
    private LocalDateTime dataHorario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_despesa", nullable = false)
    private StatusDespesaRetroativa statusDespesa = StatusDespesaRetroativa.PENDENTE_APROVACAO;

    @Column(name = "votos_necessarios", nullable = false)
    private Integer votosNecessarios;

    @CreationTimestamp
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @ManyToMany
    @JoinTable(
            name = "despesa_retroativa_participante",
            joinColumns = @JoinColumn(name = "id_despesa"),
            inverseJoinColumns = @JoinColumn(name = "id_participante")
    )
    private List<Participante> participantes = new ArrayList<>();

    public DespesaRetroativa() {
    }

    public DespesaRetroativa(List<Participante> participantes, LocalDateTime dataCriacao, Integer votosNecessarios, StatusDespesaRetroativa statusDespesa, LocalDateTime dataHorario, MeioPagamento meioPagamento, BigDecimal valor, String descricao, Grupo grupo, Long id) {
        this.participantes = participantes;
        this.dataCriacao = dataCriacao;
        this.votosNecessarios = votosNecessarios;
        this.statusDespesa = statusDespesa;
        this.dataHorario = dataHorario;
        this.meioPagamento = meioPagamento;
        this.valor = valor;
        this.descricao = descricao;
        this.grupo = grupo;
        this.id = id;
    }

    public List<Participante> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Participante> participantes) {
        this.participantes = participantes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getVotosNecessarios() {
        return votosNecessarios;
    }

    public void setVotosNecessarios(Integer votosNecessarios) {
        this.votosNecessarios = votosNecessarios;
    }

    public StatusDespesaRetroativa getStatusDespesa() {
        return statusDespesa;
    }

    public void setStatusDespesa(StatusDespesaRetroativa statusDespesa) {
        this.statusDespesa = statusDespesa;
    }

    public LocalDateTime getDataHorario() {
        return dataHorario;
    }

    public void setDataHorario(LocalDateTime dataHorario) {
        this.dataHorario = dataHorario;
    }

    public MeioPagamento getMeioPagamento() {
        return meioPagamento;
    }

    public void setMeioPagamento(MeioPagamento meioPagamento) {
        this.meioPagamento = meioPagamento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
