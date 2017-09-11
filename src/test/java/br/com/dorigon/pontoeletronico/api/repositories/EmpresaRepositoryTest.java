package br.com.dorigon.pontoeletronico.api.repositories;

import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
public class EmpresaRepositoryTest {

    @Autowired
    private EmpresaRepository mEmpresaRepository;

    private static final String CNPJ = "35863374000115";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa de Teste");
        empresa.setCnpj(CNPJ);
        this.mEmpresaRepository.save(empresa);
    }

    @After
    public void tearDown() {
        this.mEmpresaRepository.deleteAll();
    }

    @Test
    public void testBuscarPorCnpj() throws Exception {
        Empresa empresa = mEmpresaRepository.findByCnpj(CNPJ);
        assertNotNull("Empresa not null!", empresa);
        assertEquals("CNPJ is equals!", CNPJ, empresa.getCnpj());
    }

}
