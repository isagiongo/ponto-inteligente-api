package com.isagiongo.pontointeligente.api.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

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

import com.isagiongo.pontointeligente.api.entities.Empresa;
import com.isagiongo.pontointeligente.api.entities.Funcionario;
import com.isagiongo.pontointeligente.api.repositories.FuncionarioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class FuncionarioServiceTest {
	
	private static final String EMAIL = "isa@teste.com.br";

	private static final String CPF = "88899997656";

	@MockBean
	private FuncionarioRepository funcionarioRepository;
	
	@Autowired
	private FuncionarioService funcionarioService;

	@Before
	public void setUp() {
		BDDMockito.given(funcionarioRepository.save(Mockito.any(Funcionario.class))).willReturn(new Funcionario());
		BDDMockito.given(funcionarioRepository.findByCpf(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(funcionarioRepository.findByEmail(Mockito.anyString())).willReturn(new Funcionario());
		BDDMockito.given(funcionarioRepository.findById(Mockito.anyLong())).willReturn(Optional.of(new Funcionario()));
	}
	
	@Test
	public void deveBuscarFuncionarioPorCpf() {
		Optional<Funcionario> funcionario = funcionarioService.buscarPorCpf(CPF);
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void deveBuscarFuncionarioPorEmail() {
		Optional<Funcionario> funcionario = funcionarioService.buscarPorEmail(EMAIL);
		assertTrue(funcionario.isPresent());
	}
	
	@Test
	public void deveBuscarFuncionarioPorId() {
		Optional<Funcionario> funcionario = funcionarioService.buscarPorId(1L);
		assertTrue(funcionario.isPresent());
	}

	@Test
	public void devePersistirFuncionario() {
		Funcionario funcionario = funcionarioService.persistir(new Funcionario());
		assertNotNull(funcionario);
	}
}
