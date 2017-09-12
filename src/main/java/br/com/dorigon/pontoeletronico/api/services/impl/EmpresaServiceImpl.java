package br.com.dorigon.pontoeletronico.api.services.impl;

import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import br.com.dorigon.pontoeletronico.api.repositories.EmpresaRepository;
import br.com.dorigon.pontoeletronico.api.services.EmpresaService;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@Service
public class EmpresaServiceImpl implements EmpresaService {

    @Autowired
    private EmpresaRepository mEmpresaRepository;

    @Override
    public Optional<Empresa> findByCnpj(String cnpj) {
        DebugUtils.log("Geting company by CNPJ: [" + cnpj + "]");
        return Optional.ofNullable(mEmpresaRepository.findByCnpj(cnpj));
    }

    @Override
    public Empresa save(Empresa empresa) {
        DebugUtils.log("Saving comapny: " + empresa.getRazaoSocial() + " - " + empresa.getCnpj());
        return mEmpresaRepository.save(empresa);
    }

}
