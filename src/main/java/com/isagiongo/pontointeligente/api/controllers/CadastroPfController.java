package com.isagiongo.pontointeligente.api.controllers;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

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

import com.isagiongo.pontointeligente.api.dtos.CadastroPfDTO;
import com.isagiongo.pontointeligente.api.entities.Empresa;
import com.isagiongo.pontointeligente.api.entities.Funcionario;
import com.isagiongo.pontointeligente.api.enums.PerfilEnum;
import com.isagiongo.pontointeligente.api.responses.Response;
import com.isagiongo.pontointeligente.api.services.EmpresaService;
import com.isagiongo.pontointeligente.api.services.FuncionarioService;
import com.isagiongo.pontointeligente.api.utils.PasswordUtils;

@RestController
@RequestMapping("/api/cadastrar-pf")
@CrossOrigin(origins="*")
public class CadastroPfController {

	private static final Logger log = LoggerFactory.getLogger(CadastroPfController.class);
	
	@Autowired
	private FuncionarioService funcionarioService;
	
	@Autowired
	private EmpresaService empresaService;
	
	public CadastroPfController() {}
	
	/**
	 * Cadastra uma pessoa física no sistema
	 * @param cadastroPjDTO
	 * @param result
	 * @return ResponseEntity<Response<CadastroPfDto>>
	 * @throws NoSuchAlgorithmException
	 */
	public ResponseEntity<Response<CadastroPfDTO>> cadastrar(@Valid @RequestBody CadastroPfDTO cadastroPfDTO, BindingResult result) throws NoSuchAlgorithmException{
		log.info("Cadastrando PF {}", cadastroPfDTO.toString());
		Response<CadastroPfDTO> response = new Response<CadastroPfDTO>();
		
		validarDadosExistentes(cadastroPfDTO, result);
		Funcionario funcionario = converterDtoParaFuncionario(cadastroPfDTO, result);
		
		if(result.hasErrors()) {
			log.error("Erro validando dados de cadastro PF {}", result.getAllErrors());
			result.getAllErrors().forEach(error -> response.getErrors().add(error.getDefaultMessage()));
			return ResponseEntity.badRequest().body(response);
		}
		
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cadastroPfDTO.getCnpj());
		empresa.ifPresent(emp -> funcionario.setEmpresa(emp));
		
		funcionarioService.persistir(funcionario);
		
		response.setData(converterCadastroPfDTO(funcionario));
		return ResponseEntity.ok(response);
	}

	private Funcionario converterDtoParaFuncionario(CadastroPfDTO cadastroPfDTO, BindingResult result) throws NoSuchAlgorithmException {
		Funcionario funcionario = new Funcionario();
		funcionario.setNome(cadastroPfDTO.getNome());
		funcionario.setCpf(cadastroPfDTO.getCpf());
		funcionario.setEmail(cadastroPfDTO.getEmail());
		funcionario.setPerfil(PerfilEnum.ROLE_USUARIO);
		funcionario.setSenha(PasswordUtils.gerarBCrypt(cadastroPfDTO.getSenha()));
		cadastroPfDTO.getQtdHorasAlmoco().ifPresent(qtdHorasAlmoco -> funcionario.setQtdHorasAlmoco(Float.valueOf(qtdHorasAlmoco)));
		cadastroPfDTO.getQtdHorasTrabalhoDia().ifPresent(qtdHorasTrabalhoDia -> funcionario.setQtdHorasTrabalhoDia(Float.valueOf(qtdHorasTrabalhoDia)));
		cadastroPfDTO.getValorHora().ifPresent(valorHora -> funcionario.setValorHora(new BigDecimal(valorHora)));
		return funcionario;
	}

	/**
	 * Verifica se empresa ou funcionário já estão cadastrados na base de dados
	 * @param cadastroPjDTO
	 * @param result
	 */
	private void validarDadosExistentes(CadastroPfDTO cadastroPfDTO, BindingResult result) {
		
		Optional<Empresa> empresa = empresaService.buscarPorCnpj(cadastroPfDTO.getCnpj());
		
		if(!empresa.isPresent()) {
			result.addError(new ObjectError("empresa", "Empresa não cadastrada"));
		}
		
		funcionarioService.buscarPorCpf(cadastroPfDTO.getCpf())
			.ifPresent(func -> result.addError(new ObjectError("funcionario", "CPF já existente")));
		funcionarioService.buscarPorEmail(cadastroPfDTO.getEmail())
		.ifPresent(func -> result.addError(new ObjectError("funcionario", "Email já existente")));
	}
	
	private CadastroPfDTO converterCadastroPfDTO(Funcionario funcionario) {
		CadastroPfDTO cadastroPfDto = new CadastroPfDTO();
		cadastroPfDto.setId(funcionario.getId());
		cadastroPfDto.setNome(funcionario.getNome());
		cadastroPfDto.setCpf(funcionario.getCpf());
		cadastroPfDto.setEmail(funcionario.getEmail());
		cadastroPfDto.setCnpj(funcionario.getEmpresa().getCnpj());
		
		funcionario.getQtdHorasAlmocoOpt().ifPresent(qtdHorasAlmoco -> cadastroPfDto.setQtdHorasAlmoco(Optional.of(Float.toString(qtdHorasAlmoco))));
		funcionario.getQtdHorasTrabalhoDiaOpt().ifPresent(qtdHorasTrabalhoDia -> cadastroPfDto.setQtdHorasTrabalhoDia(Optional.of(Float.toString(qtdHorasTrabalhoDia))));
		funcionario.getValorHoraOpt().ifPresent(valorHora -> cadastroPfDto.setValorHora(Optional.of(valorHora.toString())));

		return cadastroPfDto;
	}
}
