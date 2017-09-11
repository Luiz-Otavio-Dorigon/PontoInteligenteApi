package br.com.dorigon.pontoeletronico.api.repositories;

import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import br.com.dorigon.pontoeletronico.api.entities.Funcionario;
import br.com.dorigon.pontoeletronico.api.enums.PerfilEnum;
import br.com.dorigon.pontoeletronico.api.utils.PasswordUtils;
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
public class FuncionarioRepositoryTest {

    @Autowired
    private FuncionarioRepository mFuncionarioRepository;

    @Autowired
    private EmpresaRepository mEmpresaRepository;

    private static final String EMAIL = "dorigon@teste.com.br";
    private static final String CPF = "04635563995";

    @Before
    public void setUp() throws Exception {
        Empresa empresa = this.mEmpresaRepository.save(obterDadosEmpresa());
        this.mFuncionarioRepository.save(obterDadosFuncionario(empresa));
    }

    @After
    public void tearDown() {
        this.mEmpresaRepository.deleteAll();
    }

    @Test
    public void testBuscarFuncionarioPorEmail() {
        Funcionario funcionario = this.mFuncionarioRepository.findByEmail(EMAIL);
        assertNotNull("Funcionário is non null!", funcionario);
        assertEquals("E-mail is equals!", EMAIL, funcionario.getEmail());
    }

    @Test
    public void testBuscarFuncionarioPorCpf() {
        Funcionario funcionario = this.mFuncionarioRepository.findByCpf(CPF);
        assertNotNull("Funcionário is non null!", funcionario);
        assertEquals("CPF is equals!", CPF, funcionario.getCpf());
    }

    @Test
    public void testBuscarFuncionarioPorCpfOrEmail() {
        Funcionario funcionario = this.mFuncionarioRepository.findByCpfOrEmail(CPF, EMAIL);
        assertNotNull("Funcionário is non null!", funcionario);
        assertEquals("CPF is equals!", CPF, funcionario.getCpf());
        assertEquals("E-mail is equals!", EMAIL, funcionario.getEmail());
    }

    @Test
    public void testBuscarFuncionarioPorCpfOrEmailParaEmailInvalido() {
        Funcionario funcionario = this.mFuncionarioRepository.findByCpfOrEmail(CPF, "teste@teste.com.br");
        assertNotNull("Funcionário is non null!", funcionario);
        assertEquals("CPF is equals!", CPF, funcionario.getCpf());
    }

    @Test
    public void testBuscarFuncionarioPorCpfOrEmailParaCpfInvalido() {
        Funcionario funcionario = this.mFuncionarioRepository.findByCpfOrEmail("08318788876", EMAIL);
        assertNotNull("Funcionário is non null!", funcionario);
        assertEquals("E-mail is equals!", EMAIL, funcionario.getEmail());
    }

    private Funcionario obterDadosFuncionario(Empresa empresa) {
        Funcionario funcionario = new Funcionario();
        funcionario.setNome("Luiz Otávio Dorigon");
        funcionario.setPerfil(PerfilEnum.ROLE_USER);
        funcionario.setSenha(PasswordUtils.geraBCrypt("123456"));
        funcionario.setCpf(CPF);
        funcionario.setEmail(EMAIL);
        funcionario.setEmpresa(empresa);
        return funcionario;
    }

    private Empresa obterDadosEmpresa() {
        Empresa empresa = new Empresa();
        empresa.setRazaoSocial("Empresa de Terste");
        empresa.setCnpj("90649337000117");
        return empresa;
    }
}
