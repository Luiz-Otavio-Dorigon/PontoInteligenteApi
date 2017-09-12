package br.com.dorigon.pontoeletronico.api.services;

import br.com.dorigon.pontoeletronico.api.entities.Lancamento;
import br.com.dorigon.pontoeletronico.api.repositories.LancamentoRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class LancamentoServiceTest {

    @MockBean
    private LancamentoRepository mLancamentoRepository;

    @Autowired
    private LancamentoService mLancamentoService;

    @Before
    public void setUp() throws Exception {
        BDDMockito
                .given(this.mLancamentoRepository.findByFuncionarioId(Mockito.anyLong(), Mockito.any(PageRequest.class)))
                .willReturn(new PageImpl<Lancamento>(new ArrayList<Lancamento>()));
        BDDMockito.given(this.mLancamentoRepository.findOne(Mockito.anyLong())).willReturn(new Lancamento());
        BDDMockito.given(this.mLancamentoRepository.save(Mockito.any(Lancamento.class))).willReturn(new Lancamento());
    }

    @Test
    public void testPersistirLancamento() {
        Lancamento lancamento = this.mLancamentoService.save(new Lancamento());

        assertNotNull(lancamento);
    }

    @Test
    public void testBuscarLancamentoPorFuncionarioId() {
        Page<Lancamento> lancamento = this.mLancamentoService.findByEmployeeId(1L, new PageRequest(0, 10));

        assertNotNull(lancamento);
    }

    @Test
    public void testBuscarLancamentoPorId() {
        Optional<Lancamento> lancamento = this.mLancamentoService.findById(1L);

        assertTrue(lancamento.isPresent());
    }

}