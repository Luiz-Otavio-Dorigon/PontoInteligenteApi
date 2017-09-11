package br.com.dorigon.pontoeletronico.api.repositories;

import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public interface EmpresaRepository extends JpaRepository<Empresa, Long> {

    @Transactional(readOnly = true)
    Empresa findByCnpj(String cnpj);
}
