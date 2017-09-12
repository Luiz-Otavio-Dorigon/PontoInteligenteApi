package br.com.dorigon.pontoeletronico.api.services;

import br.com.dorigon.pontoeletronico.api.entities.Funcionario;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
public interface FuncionarioService {

    /**
     * Save a new employee in Data Base
     *
     * @param funcionario Employee to save.
     * @return Return employee saved.
     */
    Funcionario save(Funcionario funcionario);

    /**
     * Get employee by cpf.
     *
     * @param cpf CPF of employee.
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> findByCpf(String cpf);

    /**
     * Get employee by email.
     *
     * @param email E-mail of employee
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> findByEmail(String email);

    /**
     * Get employee by id.
     *
     * @param id ID of employee
     * @return Optional<Funcionario>
     */
    Optional<Funcionario> findById(Long id);

}
