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
import com.isagiongo.pontointeligente.api.repositories.EmpresaRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaServiceTest {
	
	private static final String CNPJ = "09876688800001";

	@MockBean
	private EmpresaRepository empresaRepository;
	
	@Autowired
	private EmpresaService empresaService;
	
	@Before
	public void setUp() {
		BDDMockito.given(empresaRepository.findByCnpj(Mockito.anyString())).willReturn(new Empresa());
		BDDMockito.given(empresaRepository.save(Mockito.any(Empresa.class))).willReturn(new Empresa());
	}
	
	@Test
	public void deveBuscarEmpresaPorCnpj() {
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(CNPJ);
		assertTrue(empresa.isPresent());
	}
	
	@Test
	public void devePersistirEmpresa() {
		Empresa empresa = empresaService.persistir(new Empresa());
		assertNotNull(empresa);
	}

}
