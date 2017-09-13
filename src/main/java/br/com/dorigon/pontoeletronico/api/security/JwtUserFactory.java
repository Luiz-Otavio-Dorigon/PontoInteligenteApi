package br.com.dorigon.pontoeletronico.api.security;

import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.enums.PerfilEnum;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
public class JwtUserFactory {

    private JwtUserFactory() {
    }

    /**
     * Converte e gera um JwtUser com base nos dados de um funcionário.
     *
     * @param funcionario
     * @return JwtUser
     */
    @NotNull
    public static JwtUser create(Funcionario funcionario) {
        return new JwtUser(funcionario.getId(), funcionario.getEmail(), funcionario.getSenha(), mapToGrantedAuthorities(funcionario.getPerfil()));
    }

    /**
     * Converte o perfil do usuário para o formato utilizado pelo Spring Security.
     *
     * @param perfilEnum
     * @return List<GrantedAuthority>
     */
    private static List<GrantedAuthority> mapToGrantedAuthorities(PerfilEnum perfilEnum) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority(perfilEnum.toString()));
        return authorities;
    }
}
