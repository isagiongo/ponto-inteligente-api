package com.isagiongo.pontointeligente.api.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.isagiongo.pontointeligente.api.entities.Empresa;
import com.isagiongo.pontointeligente.api.entities.Funcionario;
import com.isagiongo.pontointeligente.api.enums.PerfilEnum;
import com.isagiongo.pontointeligente.api.utils.PasswordUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioRepositoryTest {

		private static final String EMAIL = "pupy@gmail.com";
		private static final String CPF = "00000000191";
		
		@Autowired
		private FuncionarioRepository funcionarioRepository;
		
		@Autowired
		private EmpresaRepository empresaRepository;
		
		@Before
		public void setUp() {
			Empresa empresa = this.empresaRepository.save(obterDadosEmpresa());
			this.funcionarioRepository.save(obterDadosFuncionario(empresa));
		}
		
		@After
		public void tearDown() {
			this.empresaRepository.deleteAll();
		}
		
		@Test
		public void deveBuscarFuncionarioPorEmail() {
			Funcionario funcionario = funcionarioRepository.findByEmail(EMAIL);
			Assert.assertEquals(EMAIL, funcionario.getEmail());
		}
		
		@Test
		public void deveBuscarFuncionarioPorCpf() {
			Funcionario funcionario = funcionarioRepository.findByCpf(CPF);
			Assert.assertEquals(CPF, funcionario.getCpf());
		}
		
		@Test
		public void deveBuscarFuncionarioPorCpfOuPorEmail() {
			Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, EMAIL);
			Assert.assertNotNull(funcionario);
		}
		
		@Test
		public void deveBuscarFuncionarioPorCpfOuPorEmailComEmailInvalido() {
			Funcionario funcionario = funcionarioRepository.findByCpfOrEmail(CPF, "isa@gmail.com");
			Assert.assertNotNull(funcionario);
		}
		
		@Test
		public void deveBuscarFuncionarioPorCpfOuPorEmailComCpfInvalido() {
			Funcionario funcionario = funcionarioRepository.findByCpfOrEmail("123", EMAIL);
			Assert.assertNotNull(funcionario);
		}
		
		private Funcionario obterDadosFuncionario(Empresa empresa) {
			Funcionario funcionario = new Funcionario();
			funcionario.setNome("Pupy Giongo Brandalise");
			funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
			funcionario.setSenha(PasswordUtils.gerarBCrypt("123456"));
			funcionario.setCpf(CPF);
			funcionario.setEmail(EMAIL);
			funcionario.setEmpresa(empresa);
			return funcionario;
		}
		
		private Empresa obterDadosEmpresa() {
			Empresa empresa = new Empresa();
			empresa.setRazaoSocial("Pupy e Teddy Empreendimentos LTDA");
			empresa.setCnpj("998392382783782");
			return empresa;
		}
	
}
