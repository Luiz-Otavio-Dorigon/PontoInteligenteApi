package br.com.dorigon.pontoeletronico.api.controllers;

import br.com.dorigon.pontoeletronico.api.dtos.PessoaFisicaDto;
import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.enums.PerfilEnum;
import br.com.dorigon.pontoeletronico.api.response.Response;
import br.com.dorigon.pontoeletronico.api.services.EmpresaService;
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
@CrossOrigin(origins = "*")
@RequestMapping("/api/pessoas/fisica")
public class PessoaFisicaController {

    @Autowired
    private EmpresaService mEmpresaService;

    @Autowired
    private FuncionarioService mFuncionarioService;

    public PessoaFisicaController() {
    }

    /**
     * Cadastra um funcionário pessoa física no sistema.
     *
     * @param cadastroPFDto
     * @param result
     * @return ResponseEntity<Response<PessoaFisicaDto>>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity<Response<PessoaFisicaDto>> cadastrar(@Valid @RequestBody PessoaFisicaDto cadastroPFDto, BindingResult result) throws NoSuchAlgorithmException {
        DebugUtils.log("Creating Phisical Person: " + cadastroPFDto.getNome() + " - " + cadastroPFDto.getEmail());
        Response<PessoaFisicaDto> response = new Response<>();

        validarDadosExistentes(cadastroPFDto, result);
        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPFDto, result);

        if (result.hasErrors()) {
            DebugUtils.log("Error to validate Phisical Person: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        Optional<Empresa> empresa = this.mEmpresaService.findByCnpj(cadastroPFDto.getCnpj());
        empresa.ifPresent(funcionario::setEmpresa);
        this.mFuncionarioService.save(funcionario);

        response.setData(this.converterCadastroPFDto(funcionario));
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica se a empresa está cadastrada e se o funcionário não existe na base de dados.
     *
     * @param cadastroPFDto
     * @param result
     */
    private void validarDadosExistentes(PessoaFisicaDto cadastroPFDto, BindingResult result) {
        Optional<Empresa> empresa = this.mEmpresaService.findByCnpj(cadastroPFDto.getCnpj());
        if (!empresa.isPresent()) {
            result.addError(new ObjectError("empresa", "Empresa não cadastrada."));
        }

        this.mFuncionarioService.findByCpf(cadastroPFDto.getCpf())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

        this.mFuncionarioService.findByEmail(cadastroPFDto.getEmail())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
    }

    /**
     * Converte os dados do DTO para funcionário.
     *
     * @param cadastroPFDto
     * @param result
     * @return Funcionario
     * @throws NoSuchAlgorithmException
     */
    private Funcionario converterDtoParaFuncionario(PessoaFisicaDto cadastroPFDto, BindingResult result)
            throws NoSuchAlgorithmException {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPFDto.getNome());
        funcionario.setEmail(cadastroPFDto.getEmail());
        funcionario.setCpf(cadastroPFDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.geraBCrypt(cadastroPFDto.getSenha()));
        cadastroPFDto.getQtdHorasAlmoco()
                .ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
        cadastroPFDto.getQtdHorasTrabalhoDia()
                .ifPresent(qtdHorasTrabDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabDia)));
        cadastroPFDto.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));

        return funcionario;
    }

    /**
     * Popula o DTO de cadastro com os dados do funcionário e empresa.
     *
     * @param funcionario
     * @return CadastroPFDto
     */
    private PessoaFisicaDto converterCadastroPFDto(Funcionario funcionario) {
        PessoaFisicaDto cadastroPFDto = new PessoaFisicaDto();
        cadastroPFDto.setId(funcionario.getId());
        cadastroPFDto.setNome(funcionario.getNome());
        cadastroPFDto.setEmail(funcionario.getEmail());
        cadastroPFDto.setCpf(funcionario.getCpf());
        cadastroPFDto.setCnpj(funcionario.getEmpresa().getCnpj());
        funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPFDto
                .setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
        funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(
                qtdHorasTrabDia -> cadastroPFDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabDia))));
        funcionario.getValorHoraOpt()
                .ifPresent(valorHora -> cadastroPFDto.setValorHora(Optional.of(valorHora.toString())));

        return cadastroPFDto;
    }
}
