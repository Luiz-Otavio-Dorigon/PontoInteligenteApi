package br.com.dorigon.pontoeletronico.api.controllers;

import br.com.dorigon.pontoeletronico.api.dtos.LancamentoDto;
import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.entities.Lancamento;
import br.com.dorigon.pontoeletronico.api.enums.TipoEnum;
import br.com.dorigon.pontoeletronico.api.response.Response;
import br.com.dorigon.pontoeletronico.api.services.FuncionarioService;
import br.com.dorigon.pontoeletronico.api.services.LancamentoService;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@RestController
@RequestMapping("/api/lancamentos")
@CrossOrigin(origins = "*")
public class LancamentoController {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LancamentoService lancamentoService;

    @Autowired
    private FuncionarioService funcionarioService;

    @Value("${pagination.max_items}")
    private int qtdPorPagina;

    public LancamentoController() {
    }

    /**
     * Retorna a listagem de lançamentos de um funcionário.
     *
     * @param funcionarioId
     * @return ResponseEntity<Response<LancamentoDto>>
     */
    @GetMapping(value = "/funcionario/{funcionarioId}")
    public ResponseEntity<Response<Page<LancamentoDto>>> listarPorFuncionarioId(
            @PathVariable("funcionarioId") Long funcionarioId,
            @RequestParam(value = "pag", defaultValue = "0") int pag,
            @RequestParam(value = "ord", defaultValue = "id") String ord,
            @RequestParam(value = "dir", defaultValue = "DESC") String dir) {

        DebugUtils.log("Geting releases by Employee ID [" + funcionarioId + "] and pages [" + pag + "]");
        Response<Page<LancamentoDto>> response = new Response<>();

        PageRequest pageRequest = new PageRequest(pag, this.qtdPorPagina, Sort.Direction.valueOf(dir), ord);
        Page<Lancamento> lancamentos = this.lancamentoService.findByEmployeeId(funcionarioId, pageRequest);
        Page<LancamentoDto> lancamentosDto = lancamentos.map(lancamento -> this.converterLancamentoDto(lancamento));

        response.setData(lancamentosDto);
        return ResponseEntity.ok(response);
    }

    /**
     * Retorna um lançamento por ID.
     *
     * @param id
     * @return ResponseEntity<Response<LancamentoDto>>
     */
    @GetMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> listarPorId(@PathVariable("id") Long id) {
        DebugUtils.log("Geting release by ID[" + id + "]");
        Response<LancamentoDto> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.findById(id);

        if (!lancamento.isPresent()) {
            DebugUtils.log("Release not found by ID [" + id + "]");
            response.getErrors().add("Lançamento não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.converterLancamentoDto(lancamento.get()));
        return ResponseEntity.ok(response);
    }

    /**
     * Adiciona um novo lançamento.
     *
     * @param lancamentoDto
     * @param result
     * @return ResponseEntity<Response<LancamentoDto>>
     * @throws ParseException
     */
    @PostMapping
    public ResponseEntity<Response<LancamentoDto>> adicionar(@Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        DebugUtils.log("Added release: " + lancamentoDto.getData() + " - " + lancamentoDto.getDescricao());
        Response<LancamentoDto> response = new Response<>();
        validarFuncionario(lancamentoDto, result);
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            DebugUtils.log("Error to validate release: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.save(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Atualiza os dados de um lançamento.
     *
     * @param id
     * @param lancamentoDto
     * @return ResponseEntity<Response<Lancamento>>
     * @throws ParseException
     */
    @PutMapping(value = "/{id}")
    public ResponseEntity<Response<LancamentoDto>> atualizar(@PathVariable("id") Long id, @Valid @RequestBody LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        DebugUtils.log("Updating release: " + lancamentoDto.getData() + " - " + lancamentoDto.getDescricao());
        Response<LancamentoDto> response = new Response<>();
        validarFuncionario(lancamentoDto, result);
        lancamentoDto.setId(Optional.of(id));
        Lancamento lancamento = this.converterDtoParaLancamento(lancamentoDto, result);

        if (result.hasErrors()) {
            DebugUtils.log("Error to validating release: " + result.getAllErrors());
            result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
            return ResponseEntity.badRequest().body(response);
        }

        lancamento = this.lancamentoService.save(lancamento);
        response.setData(this.converterLancamentoDto(lancamento));
        return ResponseEntity.ok(response);
    }

    /**
     * Remove um lançamento por ID.
     *
     * @param id
     * @return ResponseEntity<Response<Lancamento>>
     */
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Response<String>> remover(@PathVariable("id") Long id) {
        DebugUtils.log("Deleting release [" + id + "]");
        Response<String> response = new Response<>();
        Optional<Lancamento> lancamento = this.lancamentoService.findById(id);

        if (!lancamento.isPresent()) {
            DebugUtils.log("Error to deleting release [" + id + "]");
            response.getErrors().add("Erro ao remover lançamento. Registro não encontrado para o id " + id);
            return ResponseEntity.badRequest().body(response);
        }

        this.lancamentoService.delete(id);
        return ResponseEntity.ok(new Response<String>());
    }

    /**
     * Valida um funcionário, verificando se ele é existente e válido no
     * sistema.
     *
     * @param lancamentoDto
     * @param result
     */
    private void validarFuncionario(LancamentoDto lancamentoDto, BindingResult result) {
        if (lancamentoDto.getFuncionarioId() == null) {
            result.addError(new ObjectError("funcionario", "Funcionário não informado."));
            return;
        }

        DebugUtils.log("Validating employee [" + lancamentoDto.getFuncionarioId() + "]");
        Optional<Funcionario> funcionario = this.funcionarioService.findById(lancamentoDto.getFuncionarioId());
        if (!funcionario.isPresent()) {
            result.addError(new ObjectError("funcionario", "Funcionário não encontrado. ID inexistente."));
        }
    }

    /**
     * Converte uma entidade lançamento para seu respectivo DTO.
     *
     * @param lancamento
     * @return LancamentoDto
     */
    private LancamentoDto converterLancamentoDto(Lancamento lancamento) {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(Optional.of(lancamento.getId()));
        lancamentoDto.setData(this.dateFormat.format(lancamento.getData()));
        lancamentoDto.setTipo(lancamento.getTipo().toString());
        lancamentoDto.setDescricao(lancamento.getDescricao());
        lancamentoDto.setLocalizacao(lancamento.getLocalizacao());
        lancamentoDto.setFuncionarioId(lancamento.getFuncionario().getId());

        return lancamentoDto;
    }

    /**
     * Converte um LancamentoDto para uma entidade Lancamento.
     *
     * @param lancamentoDto
     * @param result
     * @return Lancamento
     * @throws ParseException
     */
    private Lancamento converterDtoParaLancamento(LancamentoDto lancamentoDto, BindingResult result) throws ParseException {
        Lancamento lancamento = new Lancamento();

        if (lancamentoDto.getId().isPresent()) {
            Optional<Lancamento> lanc = this.lancamentoService.findById(lancamentoDto.getId().get());
            if (lanc.isPresent()) {
                lancamento = lanc.get();
            } else {
                result.addError(new ObjectError("lancamento", "Lançamento não encontrado."));
            }
        } else {
            lancamento.setFuncionario(new Funcionario());
            lancamento.getFuncionario().setId(lancamentoDto.getFuncionarioId());
        }

        lancamento.setDescricao(lancamentoDto.getDescricao());
        lancamento.setLocalizacao(lancamentoDto.getLocalizacao());
        lancamento.setData(this.dateFormat.parse(lancamentoDto.getData()));

        if (EnumUtils.isValidEnum(TipoEnum.class, lancamentoDto.getTipo())) {
            lancamento.setTipo(TipoEnum.valueOf(lancamentoDto.getTipo()));
        } else {
            result.addError(new ObjectError("tipo", "Tipo inválido."));
        }

        return lancamento;
    }

}