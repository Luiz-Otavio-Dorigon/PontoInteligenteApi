package br.com.dorigon.pontoeletronico.api.services;

import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import br.com.dorigon.pontoeletronico.api.repositories.EmpresaRepository;
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
public class EmpresaServiceTest {

    @MockBean
    private EmpresaRepository mEmpresaRepository;

    @Autowired
    private EmpresaService mEmpresaService;

    private static final String CNPJ = "51463645000100";

    @Before
    public void setUp() throws Exception {
        BDDMockito.given(this.mEmpresaRepository.findByCnpj(Mockito.anyString())).willReturn(new Empresa());
        BDDMockito.given(this.mEmpresaRepository.save(Mockito.any(Empresa.class))).willReturn(new Empresa());
    }

    @Test
    public void testBuscarEmpresaPorCnpj() {
        Optional<Empresa> empresa = this.mEmpresaService.findByCnpj(CNPJ);

        assertTrue(empresa.isPresent());
    }

    @Test
    public void testPersistirEmpresa() {
        Empresa empresa = this.mEmpresaService.save(new Empresa());

        assertNotNull(empresa);
    }

}