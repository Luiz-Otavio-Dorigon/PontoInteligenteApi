package br.com.dorigon.pontoeletronico.api.repositories;

import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */

@Transactional(readOnly = true)
public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Funcionario findByCpf(String cpf);

    Funcionario findByEmail(String email);

    Funcionario findByCpfOrEmail(String cpf, String email);

}
