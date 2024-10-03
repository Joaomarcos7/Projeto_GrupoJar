package br.edu.ifpb.pweb2.Jar.service;

import br.edu.ifpb.pweb2.Jar.model.Aluno;
import br.edu.ifpb.pweb2.Jar.model.Authority;
import br.edu.ifpb.pweb2.Jar.model.User;
import br.edu.ifpb.pweb2.Jar.repository.AlunoRepository;
import br.edu.ifpb.pweb2.Jar.repository.AuthorityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Autowired
    private AuthorityRepository authorityRepository;


    @Transactional
    public Aluno save(Aluno aluno) {
        Aluno alunoachado = alunoRepository.findByEmail(aluno.getEmail());

        if (alunoachado != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        aluno.setPassword(passwordEncoder.encode(aluno.getPassword()));

        Authority authority = new Authority();
        authority.setId(new Authority.AuthorityId(aluno.getUsername(), "ROLE_ALUNO"));
        authority.setUsername(aluno);
        authority.setAuthority("ROLE_ALUNO");
        Aluno alunoSaved =  this.alunoRepository.save(aluno);
        this.authorityRepository.save(authority);
        return alunoSaved;
    }

    public List<Aluno> findAll() {
        return alunoRepository.findAll();
    }

    public Optional<Aluno> findById(Long id) { return alunoRepository.findById(id); }

    public Aluno findByEmail(String email){return  alunoRepository.findByEmail(email); }

    public Aluno findByUsername(String username) { return alunoRepository.findByUsername(username); }

}
