package br.com.dorigon.pontoeletronico.api.controllers;

import br.com.dorigon.pontoeletronico.api.dtos.LancamentoDto;
import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.entities.Lancamento;
import br.com.dorigon.pontoeletronico.api.enums.TipoEnum;
import br.com.dorigon.pontoeletronico.api.services.FuncionarioService;
import br.com.dorigon.pontoeletronico.api.services.LancamentoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class LancamentoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private LancamentoService lancamentoService;

    @MockBean
    private FuncionarioService funcionarioService;

    private static final String URL_BASE = "/api/lancamentos/";
    private static final Long ID_FUNCIONARIO = 2L;
    private static final Long ID_LANCAMENTO = 1L;
    private static final String TIPO = TipoEnum.INICIO_TRABALHO.name();
    private static final Date DATA = Calendar.getInstance().getTime();

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Test
    @WithMockUser
    public void testCadastrarLancamento() throws Exception {
        Lancamento lancamento = obterDadosLancamento();
        BDDMockito.given(this.funcionarioService.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
        BDDMockito.given(this.lancamentoService.save(Mockito.any(Lancamento.class))).willReturn(lancamento);

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                .content(this.obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(ID_LANCAMENTO))
                .andExpect(jsonPath("$.data.tipo").value(TIPO))
                .andExpect(jsonPath("$.data.data").value(this.dateFormat.format(DATA)))
                .andExpect(jsonPath("$.data.funcionarioId").value(ID_FUNCIONARIO))
                .andExpect(jsonPath("$.errors").isEmpty());
    }

    @Test
    @WithMockUser
    public void testCadastrarLancamentoFuncionarioIdInvalido() throws Exception {
        BDDMockito.given(this.funcionarioService.findById(Mockito.anyLong())).willReturn(Optional.empty());

        mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
                .content(this.obterJsonRequisicaoPost())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").value("Funcionário não encontrado. ID inexistente."))
                .andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    @WithMockUser(username = "admin@dorigon.com.br", roles = {"ADMIN"})
    public void testRemoverLancamento() throws Exception {
        BDDMockito.given(this.lancamentoService.findById(Mockito.anyLong())).willReturn(Optional.of(new Lancamento()));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    public void testRemoverLancamentoAcessoNegado() throws Exception {
        BDDMockito.given(this.lancamentoService.findById(Mockito.anyLong())).willReturn(Optional.of(new Lancamento()));

        mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_LANCAMENTO)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private String obterJsonRequisicaoPost() throws JsonProcessingException {
        LancamentoDto lancamentoDto = new LancamentoDto();
        lancamentoDto.setId(null);
        lancamentoDto.setData(this.dateFormat.format(DATA));
        lancamentoDto.setTipo(TIPO);
        lancamentoDto.setFuncionarioId(ID_FUNCIONARIO);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(lancamentoDto);
    }

    private Lancamento obterDadosLancamento() {
        Lancamento lancamento = new Lancamento();
        lancamento.setId(ID_LANCAMENTO);
        lancamento.setData(DATA);
        lancamento.setTipo(TipoEnum.valueOf(TIPO));
        lancamento.setFuncionario(new Funcionario());
        lancamento.getFuncionario().setId(ID_FUNCIONARIO);
        return lancamento;
    }

}