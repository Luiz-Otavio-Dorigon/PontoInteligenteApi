package br.com.dorigon.pontoeletronico.api.services.impl;

import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.repositories.FuncionarioRepository;
import br.com.dorigon.pontoeletronico.api.services.FuncionarioService;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    @Autowired
    private FuncionarioRepository mFuncionarioRepository;

    @Override
    public Funcionario save(Funcionario funcionario) {
        DebugUtils.log("Saiving employee: " + funcionario.getNome() + " - " + funcionario.getCpf());
        return mFuncionarioRepository.save(funcionario);
    }

    @Override
    public Optional<Funcionario> findByCpf(String cpf) {
        DebugUtils.log("Get employee by CPF [" + cpf + "]");
        return Optional.ofNullable(mFuncionarioRepository.findByCpf(cpf));
    }

    @Override
    public Optional<Funcionario> findByEmail(String email) {
        DebugUtils.log("Get employee by E-mail [" + email + "]");
        return Optional.ofNullable(mFuncionarioRepository.findByEmail(email));
    }

    @Override
    public Optional<Funcionario> findById(Long id) {
        DebugUtils.log("Get employee by ID [" + id + "]");
        return Optional.ofNullable(mFuncionarioRepository.findOne(id));
    }
}
