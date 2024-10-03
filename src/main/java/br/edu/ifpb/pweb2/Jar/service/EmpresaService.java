package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Authority;
import br.edu.ifpb.pweb2.Jar.model.Empresa;
import br.edu.ifpb.pweb2.Jar.model.OfertaEstagio;
import br.edu.ifpb.pweb2.Jar.repository.AuthorityRepository;
import br.edu.ifpb.pweb2.Jar.repository.EmpresaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthorityRepository authorityRepository;

    @Transactional
    public Empresa save(Empresa empresa) {

        Empresa companyCnpj = this.empresaRepository.findByCnpj(empresa.getCnpj());

        if (companyCnpj != null) {
            throw new IllegalArgumentException("CNPJ já cadastrado");
        }

        empresa.setPassword(passwordEncoder.encode(empresa.getPassword()));

        Authority authority = new Authority();
        authority.setId(new Authority.AuthorityId(empresa.getUsername(), "ROLE_EMPRESA"));
        authority.setUsername(empresa);
        authority.setAuthority("ROlE_EMPRESA");
        Empresa empresaSaved = this.empresaRepository.save(empresa);
        authorityRepository.save(authority);
        return empresaSaved;
    }

    public List<Empresa> findAll() {
        return empresaRepository.findAll();
    }

    public Page<Empresa> findAllPaged(Pageable page) {
        return empresaRepository.findAll(page);
    }

    public Optional<Empresa> findById(Long id) {
        return empresaRepository.findById(id);
    }

    public void deleteById(Long id) {
        empresaRepository.deleteById(id);
    }    

    public Empresa findByCnpj(String cnpj) { return empresaRepository.findByCnpj(cnpj); }

    public Empresa findByUsername(String username) { return empresaRepository.findByUsername(username).orElseThrow(); }
}
