package br.com.dorigon.pontoeletronico.api.services;

import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.repositories.FuncionarioRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class FuncionarioServiceTest {

    @MockBean
    private FuncionarioRepository mFuncionarioRepository;

    @Autowired
    private FuncionarioService mFuncionarioService;

    @Before
    public void setUp() throws Exception {
        BDDMockito.given(this.mFuncionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
        BDDMockito.given(this.mFuncionarioRepository.findOne(Mockito.anyLong())).willReturn(new Funcionario());
        BDDMockito.given(this.mFuncionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
        BDDMockito.given(this.mFuncionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
    }

    @Test
    public void testPersistirFuncionario() {
        Funcionario funcionario = this.mFuncionarioService.save(new Funcionario());

        assertNotNull(funcionario);
    }

    @Test
    public void testBuscarFuncionarioPorId() {
        Optional<Funcionario> funcionario = this.mFuncionarioService.findById(1L);

        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        Optional<Funcionario> funcionario = this.mFuncionarioService.findByEmail("email@email.com");

        assertTrue(funcionario.isPresent());
    }

    @Test
    public void testBuscarFuncionarioPorCpf() {
        Optional<Funcionario> funcionario = this.mFuncionarioService.findByCpf("24291173474");

        assertTrue(funcionario.isPresent());
    }

}