package br.com.dorigon.pontoeletronico.api.services;

import br.com.dorigon.pontoeletronico.api.entities.Empresa;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public interface EmpresaService {

    /**
     * Get an company by CNPJ.
     *
     * @param cnpj CNPJ.
     * @return Optional<Empresa>.
     */
    Optional<Empresa> findByCnpj(String cnpj);

    /**
     * Save new company in Data Base.
     *
     * @param empresa Company to save.
     * @return Return company saved.
     */
    Empresa save(Empresa empresa);
}
