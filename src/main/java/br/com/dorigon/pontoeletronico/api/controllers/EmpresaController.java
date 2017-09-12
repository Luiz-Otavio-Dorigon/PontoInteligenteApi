package br.com.dorigon.pontoeletronico.api.controllers;

import br.com.dorigon.pontoeletronico.api.dtos.EmpresaDto;
import br.com.dorigon.pontoeletronico.api.entities.Empresa;
import br.com.dorigon.pontoeletronico.api.response.Response;
import br.com.dorigon.pontoeletronico.api.services.EmpresaService;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 12/09/17.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/empresas")
public class EmpresaController {

    @Autowired
    private EmpresaService mEmpresaService;

    public EmpresaController() {
    }

    /**
     * Find company by CNPJ.
     *
     * @param cnpj CNPJ to get comany.
     * @return ResponseEntity<Response<EmpresaDto>>
     */
    @GetMapping(value = "/cnpj/{cnpj}")
    public ResponseEntity<Response<EmpresaDto>> findByCnpj(@PathVariable("cnpj") String cnpj) {
        DebugUtils.log("Geting Company by CNPJ: [" + cnpj + "]");
        Response<EmpresaDto> response = new Response<>();

        Optional<Empresa> empresa = mEmpresaService.findByCnpj(cnpj);

        if (!empresa.isPresent()) {
            DebugUtils.log("Error to get company by CNPJ: " + cnpj);
            response.getErrors().add("Empresa não encontrada com o CNPJ: " + cnpj);
            return ResponseEntity.badRequest().body(response);
        }

        response.setData(this.convert(empresa.get()));
        return ResponseEntity.ok(response);
    }

    /**
     * Convert Company into EmpresaDto.
     *
     * @param empresa Company to convert.
     * @return Return EmpresaDto.
     */
    private EmpresaDto convert(Empresa empresa) {

        EmpresaDto empresaDto = new EmpresaDto();
        empresaDto.setId(empresa.getId());
        empresaDto.setCnpj(empresa.getCnpj());
        empresaDto.setRazaoSocial(empresa.getRazaoSocial());

        return empresaDto;
    }
}
