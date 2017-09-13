package br.com.dorigon.pontoeletronico.api.services.impl;

import br.com.dorigon.pontoeletronico.api.entities.Lancamento;
import br.com.dorigon.pontoeletronico.api.repositories.LancamentoRepository;
import br.com.dorigon.pontoeletronico.api.services.LancamentoService;
import br.com.dorigon.pontoeletronico.api.utils.DebugUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Luiz Otávio Dorigon <luiz.otavio.dorigon@gmail.com>  on 11/09/17.
 */
@Service
public class LancamentoServiceImpl implements LancamentoService {

    @Autowired
    private LancamentoRepository mLancamentoRepository;

    @Override
    public Page<Lancamento> findByEmployeeId(Long employeeId, PageRequest pageRequest) {
        DebugUtils.log("Get employee by ID [" + employeeId + "]");
        return mLancamentoRepository.findByFuncionarioId(employeeId, pageRequest);
    }

    @Override
    @Cacheable("lancamentoPorId")
    public Optional<Lancamento> findById(Long id) {
        DebugUtils.log("Get employee by ID [" + id + "]");
        return Optional.ofNullable(mLancamentoRepository.findOne(id));
    }

    @Override
    @CachePut("lancamentoPorId")
    public Lancamento save(Lancamento lancamento) {
        DebugUtils.log("Saving release " + lancamento.getTipo() + " - " + lancamento.getDescricao());
        return mLancamentoRepository.save(lancamento);
    }

    @Override
    public void delete(Long id) {
        DebugUtils.log("Deleting release [" + id + "]");
        mLancamentoRepository.delete(id);
    }
}
