package com.isagiongo.pontointeligente.api.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

public class CadastroPjDTO {
	
	private Long id;
	
	@NotEmpty(message="Nome não pode ser vazio")
	@Length(min=3, max=200, message= "Nome deve conter entre 3 e 200 caracteres")
	private String nome;
	
	@NotEmpty(message="CPF não pode ser vazio")
	@CPF(message="CPF inválido")
	private String cpf;
	
	@NotEmpty(message="Email não pode ser vazio")
	@Length(min=3, max=200, message= "Email deve conter entre 3 e 200 caracteres")
	@Email(message="Email inválido")
	private String email;
	
	@NotEmpty(message="Senha não pode ser vazio")
	@Length(min=6, message= "Senha deve conter, no mínimo, 6 caracteres")
	private String senha;
	
	@NotEmpty(message="CNPJ não pode ser vazio")
	@CNPJ(message="CNPJ inválido")
	private String cnpj;
	
	@NotEmpty(message="Razão Social não pode ser vazio")
	@Length(min=3, max=200, message= "Razão Social deve conter entre 3 e 200 caracteres")
	private String razaoSocial;
	
	public CadastroPjDTO() {}

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
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

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@Override
	public String toString() {
		return "CadastroPjDTO [id=" + id + ", nome=" + nome + ", cpf=" + cpf + ", email=" + email + ", senha=" + senha
				+ ", cnpj=" + cnpj + ", razaoSocial=" + razaoSocial + "]";
	}
}