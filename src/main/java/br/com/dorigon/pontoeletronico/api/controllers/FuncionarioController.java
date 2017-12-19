package br.com.dorigon.pontoeletronico.api.controllers;

import br.com.dorigon.pontoeletronico.api.dtos.FuncionarioDto;
import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.response.Response;
import br.com.dorigon.pontoeletronico.api.services.FuncionarioService;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import br.com.dorigon.pontoeletronico.api.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@RestController
@RequestMapping("/api/funcionarios")
@CrossOrigin(origins = "*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService mFuncionarioService;

    public FuncionarioController() {
    }

    /**
     * Atualiza os dados de um funcionário.
     *
     * @param id
     * @param funcionarioDto
     * @param result
     * @return ResponseEntity<Response<FuncionarioDto>>
     * @throws NoSuchAlgorithmException
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<FuncionarioDto>> atualizar(@PathVariable("id") Long id, @Valid @RequestBody FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
        DebugUtils.log("Updating employee: " + funcionarioDto.getNome() + " - " + funcionarioDto.getEmail());
        Response<FuncionarioDto> response = new Response<>();

        Optional<Funcionario> funcionario = this.mFuncionarioService.findById(id);
        if (!funcionario.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado."));
        }

        this.atualizarDadosFuncionario(funcionario.get(), funcionarioDto, result);

        if (result.hasErrors()) {
            DebugUtils.log("Error to validate employee: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.mFuncionarioService.save(funcionario.get());
        response.setData(this.converterFuncionarioDto(funcionario.get()));

        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza os dados do funcionário com base nos dados encontrados no DTO.
     *
     * @param funcionario
     * @param funcionarioDto
     * @param result
     * @throws NoSuchAlgorithmException
     */
    private void atualizarDadosFuncionario(Funcionario funcionario, FuncionarioDto funcionarioDto, BindingResult result) throws NoSuchAlgorithmException {
        funcionario.setNome(funcionarioDto.getNome());

        if (!funcionario.getEmail().equals(funcionarioDto.getEmail())) {
            this.mFuncionarioService.findByEmail(funcionarioDto.getEmail()).ifPresent(func -> result.addError(new ObjectError("email", "Email já existente.")));
            funcionario.setEmail(funcionarioDto.getEmail());
        }

        funcionario.setQtdHorasAlmoco(null);
        funcionarioDto.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));

        funcionario.setQtdHorasTrabalhoDia(null);
        funcionarioDto.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));

        funcionario.setValorHora(null);
        funcionarioDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        if (funcionarioDto.getSenha().isPresent()) {
            funcionario.setSenha(PasswordUtils.geraBCrypt(funcionarioDto.getSenha().get()));
        }
    }

    /**
     * Retorna um DTO com os dados de um funcionário.
     *
     * @param funcionario
     * @return FuncionarioDto
     */
    private FuncionarioDto converterFuncionarioDto(Funcionario funcionario) {
        FuncionarioDto funcionarioDto = new FuncionarioDto();
        funcionarioDto.setId(funcionario.getId());
        funcionarioDto.setEmail(funcionario.getEmail());
        funcionarioDto.setNome(funcionario.getNome());
        funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> funcionarioDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
        funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabDia -> funcionarioDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
        funcionario.getValorHoraOpt().ifPresent(valorHora -> funcionarioDto.setValorHora(Optional.of(valorHora.toString())));

        return funcionarioDto;
    }

}