package br.com.dorigon.pontoeletronico.api.repositories;

import br.com.dorigon.pontoeletronico.api.entities.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@Transactional(readOnly = true)
@NamedQueries(
        {@NamedQuery(name = "LancamentoRepository.findByFuncionarioId", query = "SELECT l FROM Lancamento l WHERE l.funcionario.id = :funcionarioId")}
)
public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

    List<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId);

    Page<Lancamento> findByFuncionarioId(@Param("funcionarioId") Long funcionarioId, Pageable pageable);

}
