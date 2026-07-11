// DespesaRetroativaRepository.java
package com.bradesco.sharewallet.Repositories;

import com.bradesco.sharewallet.Entities.DespesaRetroativa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DespesaRetroativaRepository extends JpaRepository<DespesaRetroativa, Long> {
    List<DespesaRetroativa> findByGrupoId(Long grupoId);
}