package com.isagiongo.pontointeligente.api.controllers;

import java.security.NoSuchAlgorithmException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.isagiongo.pontointeligente.api.dtos.CadastroPjDTO;
import com.isagiongo.pontointeligente.api.entities.Empresa;
import com.isagiongo.pontointeligente.api.entities.Funcionario;
import com.isagiongo.pontointeligente.api.enums.PerfilEnum;
import com.isagiongo.pontointeligente.api.responses.Response;
import com.isagiongo.pontointeligente.api.services.EmpresaService;
import com.isagiongo.pontointeligente.api.services.FuncionarioService;
import com.isagiongo.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pj")
@CrossOrigin(origins="*")
public class CadastroPjController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPjController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPjController() {}
	
	/**
	 * Cadastra uma pessoa Jurídica no sistema
	 * @param cadastroPjDTO
	 * @param result
	 * @return ResponseEntity<Response<CadastroPjDto>>
	 * @throws NoSuchAlgorithmException
	 */
	public ResponseEntity<Response<CadastroPjDTO>> cadastrar(@Valid @RequestBody CadastroPjDTO cadastroPjDTO, BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando PJ {}", cadastroPjDTO.toString());
		Response<CadastroPjDTO> response = new Response<CadastroPjDTO>();
		
		validarDadosExistentes(cadastroPjDTO, result);
		Empresa empresa = converterDtoParaEmpresa(cadastroPjDTO);
		Funcionario funcionario = converterDtoParaFuncionario(cadastroPjDTO, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro PJ {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		empresaService.persistir(empresa);
		funcionario.setEmpresa(empresa);
		funcionarioService.persistir(funcionario);
		
		response.setData(converterCadastroPjDTO(funcionario));
		return ResponseEntity.ok(response);
	}


	private Empresa converterDtoParaEmpresa(CadastroPjDTO cadastroPjDTO) {
		Empresa empresa = new Empresa();
		empresa.setCnpj(cadastroPjDTO.getCnpj());
		empresa.setRazaoSocial(cadastroPjDTO.getRazaoSocial());
		return empresa;
	}

	private Funcionario converterDtoParaFuncionario(CadastroPjDTO cadastroPjDTO, BindingResult result) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPjDTO.getNome());
		funcionario.setCpf(cadastroPjDTO.getCpf());
		funcionario.setEmail(cadastroPjDTO.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_ADMIN);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPjDTO.getSenha()));
		return funcionario;
	}

	/**
	 * Verifica se empresa ou funcionário já estão cadastrados na base de dados
	 * @param cadastroPjDTO
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPjDTO cadastroPjDTO, BindingResult result) {
		empresaService.buscarPorCnpj(cadastroPjDTO.getCnpj())
			.ifPresent(emp -> result.addError(new ObjectError("empresa", "CNPJ já existente")));
		funcionarioService.buscarPorCpf(cadastroPjDTO.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente")));
		funcionarioService.buscarPorEmail(cadastroPjDTO.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente")));
	}
	
	private CadastroPjDTO converterCadastroPjDTO(Funcionario funcionario) {
		CadastroPjDTO cadastroPjDto = new CadastroPjDTO();
		cadastroPjDto.setId(funcionario.getId());
		cadastroPjDto.setNome(funcionario.getNome());
		cadastroPjDto.setCpf(funcionario.getCpf());
		cadastroPjDto.setEmail(funcionario.getEmail());
		cadastroPjDto.setRazaoSocial(funcionario.getEmpresa().getRazaoSocial());
		cadastroPjDto.setCnpj(funcionario.getEmpresa().getCnpj());
		return cadastroPjDto;
	}
}
