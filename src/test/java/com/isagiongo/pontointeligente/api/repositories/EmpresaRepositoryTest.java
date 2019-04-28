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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EmpresaRepositoryTest {

	private static final String RAZAO_SOCIAL = "Teddy e Pupy Serviços Diversos LTDA";
	private static final String CNPJ = "009987651000";
	
	@Autowired
	private EmpresaRepository empresaRepository;
	
	@Before
	public void setUp() {
		Empresa empresa = new Empresa();
		empresa.setRazaoSocial(RAZAO_SOCIAL);
		empresa.setCnpj(CNPJ);
		this.empresaRepository.save(empresa);
	}
	
	@After
	public void tearDown() {
		this.empresaRepository.deleteAll();
	}
	
	@Test
	public void deveBuscarPorCnpj() {
		Empresa empresa = empresaRepository.findByCnpj(CNPJ);
		Assert.assertEquals(CNPJ, empresa.getCnpj());
	}
}
