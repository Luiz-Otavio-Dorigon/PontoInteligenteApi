package br.com.dorigon.pontoeletronico.api.controllers;

import br.com.dorigon.pontoeletronico.api.dtos.PessoaJuridicaDto;
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
import java.security.NoSuchAlgorithmException;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/pessoas/juridica")
public class PessoaJuridicaController {

    @Autowired
    private FuncionarioService mFuncionarioService;

    @Autowired
    private EmpresaService mEmpresaService;

    public PessoaJuridicaController() {
    }

    /**
     * Cadastra uma pessoa jurídica no sistema.
     *
     * @param cadastroPJDto
     * @param result
     * @return ResponseEntity<Response<CadastroPJDto>>
     * @throws NoSuchAlgorithmException
     */
    @PostMapping
    public ResponseEntity<Response<PessoaJuridicaDto>> cadastrar(@Valid @RequestBody PessoaJuridicaDto cadastroPJDto, BindingResult result) throws NoSuchAlgorithmException {
        DebugUtils.log("Creating Legal Person: " + cadastroPJDto.getNome() + " - " + cadastroPJDto.getEmail());
        Response<PessoaJuridicaDto> response = new Response<>();

        validarDadosExistentes(cadastroPJDto, result);

        Empresa empresa = this.converterDtoParaEmpresa(cadastroPJDto);
        Funcionario funcionario = this.converterDtoParaFuncionario(cadastroPJDto, result);

        if (result.hasErrors()) {
            DebugUtils.log("Error to valida Legal Person: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        this.mEmpresaService.save(empresa);
        funcionario.setEmpresa(empresa);
        this.mFuncionarioService.save(funcionario);

        response.setData(this.converterCadastroPJDto(funcionario));
        return ResponseEntity.ok(response);
    }

    /**
     * Verifica se a empresa ou funcionário já existem na base de dados.
     *
     * @param cadastroPJDto
     * @param result
     */
    private void validarDadosExistentes(PessoaJuridicaDto cadastroPJDto, BindingResult result) {
        this.mEmpresaService.findByCnpj(cadastroPJDto.getCnpj())
                .ifPresent(emp -> result.addError(new ObjectError("empresa", "Empresa já existente.")));

        this.mFuncionarioService.findByCpf(cadastroPJDto.getCpf())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente.")));

        this.mFuncionarioService.findByEmail(cadastroPJDto.getEmail())
                .ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente.")));
    }

    /**
     * Converte os dados do DTO para empresa.
     *
     * @param cadastroPJDto
     * @return Empresa
     */
    private Empresa converterDtoParaEmpresa(PessoaJuridicaDto cadastroPJDto) {
        Empresa empresa = new Empresa();
        empresa.setCnpj(cadastroPJDto.getCnpj());
        empresa.setRazaoSocial(cadastroPJDto.getRazaoSocial());

        return empresa;
    }

    /**
     * Converte os dados do DTO para funcionário.
     *
     * @param cadastroPJDto
     * @param result
     * @return Funcionario
     * @throws NoSuchAlgorithmException
     */
    private Funcionario converterDtoParaFuncionario(PessoaJuridicaDto cadastroPJDto, BindingResult result)
            throws NoSuchAlgorithmException {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome(cadastroPJDto.getNome());
        funcionario.setEmail(cadastroPJDto.getEmail());
        funcionario.setCpf(cadastroPJDto.getCpf());
        funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
        funcionario.setSenha(PasswordUtils.geraBCrypt(cadastroPJDto.getSenha()));

        return funcionario;
    }

    /**
     * Popula o DTO de cadastro com os dados do funcionário e empresa.
     *
     * @param funcionario
     * @return CadastroPJDto
     */
    private PessoaJuridicaDto converterCadastroPJDto(Funcionario funcionario) {
        PessoaJuridicaDto cadastroPJDto = new PessoaJuridicaDto();
        cadastroPJDto.setId(funcionario.getId());
        cadastroPJDto.setNome(funcionario.getNome());
        cadastroPJDto.setEmail(funcionario.getEmail());
        cadastroPJDto.setCpf(funcionario.getCpf());
        cadastroPJDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
        cadastroPJDto.setCnpj(funcionario.getEmpresa().getCnpj());

        return cadastroPJDto;
    }
}
