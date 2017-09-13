package br.com.dorigon.pontoeletronico.api.security.controllers;

import br.com.dorigon.pontoeletronico.api.response.Response;
import br.com.dorigon.pontoeletronico.api.security.dtos.JwtAuthenticationDto;
import br.com.dorigon.pontoeletronico.api.security.dtos.TokenDto;
import br.com.dorigon.pontoeletronico.api.security.utils.JwtTokenUtil;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private static final String TOKEN_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Autowired
    private AuthenticationManager mAuthenticationManager;

    @Autowired
    private JwtTokenUtil mJwtTokenUtil;

    @Autowired
    private UserDetailsService mUserDetailsService;

    /**
     * Gera e retorna um novo token JWT.
     *
     * @param authenticationDto
     * @param result
     * @return ResponseEntity<Response<TokenDto>>
     * @throws AuthenticationException
     */
    @PostMapping
    public ResponseEntity<Response<TokenDto>> gerarTokenJwt(
            @Valid @RequestBody JwtAuthenticationDto authenticationDto, BindingResult result)
            throws AuthenticationException {
        Response<TokenDto> response = new Response<TokenDto>();

        if (result.hasErrors()) {
            DebugUtils.log(this.getClass(), "Erro validando lançamento: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        DebugUtils.log(this.getClass(), "Gerando token para o email: " + authenticationDto.getEmail());
        Authentication authentication = mAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationDto.getEmail(), authenticationDto.getSenha()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = mUserDetailsService.loadUserByUsername(authenticationDto.getEmail());
        String token = mJwtTokenUtil.obterToken(userDetails);
        response.setData(new TokenDto(token));

        return ResponseEntity.ok(response);
    }

    /**
     * Gera um novo token com uma nova data de expiração.
     *
     * @param request
     * @return ResponseEntity<Response<TokenDto>>
     */
    @PostMapping(value = "/refresh")
    public ResponseEntity<Response<TokenDto>> gerarRefreshTokenJwt(HttpServletRequest request) {
        DebugUtils.log(this.getClass(), "Gerando refresh token JWT.");
        Response<TokenDto> response = new Response<TokenDto>();
        Optional<String> token = Optional.ofNullable(request.getHeader(TOKEN_HEADER));

        if (token.isPresent() && token.get().startsWith(BEARER_PREFIX)) {
            token = Optional.of(token.get().substring(7));
        }

        if (!token.isPresent()) {
            response.getErrors().add("Token não informado.");
        } else if (!mJwtTokenUtil.tokenValido(token.get())) {
            response.getErrors().add("Token inválido ou expirado.");
        }

        if (!response.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(response);
        }

        String refreshedToken = mJwtTokenUtil.refreshToken(token.get());
        response.setData(new TokenDto(refreshedToken));
        return ResponseEntity.ok(response);
    }

}