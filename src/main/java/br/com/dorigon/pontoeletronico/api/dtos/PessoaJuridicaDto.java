package br.com.dorigon.pontoeletronico.api.dtos;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
public class PessoaJuridicaDto {

    private Long id;

    @NotEmpty(message = "O nome é obrigatório!")
    @Length(min = 3, max = 200, message = "O nome deve conter entre 3 e 200 caracteres.")
    private String nome;

    @NotEmpty(message = "E-mail é obrigatório.")
    @Length(min = 5, max = 200, message = "Email deve conter entre 5 e 200 caracteres.")
    @Email(message = "Email inválido.")
    private String email;

    @NotEmpty(message = "Senha é obrigatória.")
    private String senha;

    @NotEmpty(message = "CPF é obrigatório.")
    @CPF(message = "CPF inválido")
    private String cpf;

    @NotEmpty(message = "Razão social é obrigatória.")
    @Length(min = 5, max = 200, message = "Razão social deve conter entre 5 e 200 caracteres.")
    private String razaoSocial;

    @NotEmpty(message = "CNPJ é obrigatório.")
    @CNPJ(message = "CNPJ inválido.")
    private String cnpj;

    public PessoaJuridicaDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
