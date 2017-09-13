package br.com.dorigon.pontoeletronico.api.security.services;

import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.security.JwtUserFactory;
import br.com.dorigon.pontoeletronico.api.services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private FuncionarioService mFuncionarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Funcionario> funcionario = mFuncionarioService.findByEmail(username);

        if (funcionario.isPresent()) {
            return JwtUserFactory.create(funcionario.get());
        }

        throw new UsernameNotFoundException("Email não encontrado.");
    }
}
