package br.com.dorigon.pontoeletronico.api.services;

import br.com.dorigon.pontoeletronico.api.entities.Lancamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public interface LancamentoService {

    /**
     * Get an list paginated of a releases of employee.
     *
     * @param employeeId  ID of employee.
     * @param pageRequest Prams to paginate.
     * @return Page<Lancamento>
     */
    Page<Lancamento> findByEmployeeId(Long employeeId, PageRequest pageRequest);

    /**
     * Get an release by id.
     *
     * @param id ID of employee
     * @return Optional<Lancamento>
     */
    Optional<Lancamento> findById(Long id);

    /**
     * Save an release in Data Base
     *
     * @param lancamento Release to save.
     * @return Release was saved.
     */
    Lancamento save(Lancamento lancamento);

    /**
     * Delete an release in Data Base.
     *
     * @param id ID of release to delete.
     */
    void delete(Long id);
}
