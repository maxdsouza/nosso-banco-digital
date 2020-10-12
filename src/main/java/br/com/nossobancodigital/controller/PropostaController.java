package br.com.nossobancodigital.controller;

import br.com.nossobancodigital.entity.PropostaInfosBasicas;
import br.com.nossobancodigital.repository.PropostaInfosBasicasRepository;
import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/clientepf")
public class PropostaController {

    private final PropostaInfosBasicasRepository repository;

    PropostaController(PropostaInfosBasicasRepository propostaInfosBasicasRepository) {
        this.repository = propostaInfosBasicasRepository;
    }

    @PostMapping
    public ResponseEntity<PropostaInfosBasicas> salvar(@Valid @RequestBody PropostaInfosBasicas propostaInfosBasicas) {
        PropostaInfosBasicas savedPropostaInfosBasicas = repository.save(propostaInfosBasicas);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedPropostaInfosBasicas.getId()).toUri();

        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public List<PropostaInfosBasicas> buscarTodos() {
        return repository.findAll();
    }

    @GetMapping(path = {"/{id}"})
    public ResponseEntity<PropostaInfosBasicas> buscarPorId(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<PropostaInfosBasicas> atualizar(
        @PathVariable("id") long id,
        @RequestBody @Valid PropostaInfosBasicas propostaInfosBasicas
    ) {
        return repository.findById(id)
                .map(record -> {
                    record.setNome(propostaInfosBasicas.getNome());
                    record.setSobrenome(propostaInfosBasicas.getSobrenome());
                    record.setEmail(propostaInfosBasicas.getEmail());
                    record.setDataNascimento(propostaInfosBasicas.getDataNascimento());
                    record.setCpf(propostaInfosBasicas.getCpf());
                    record.setEndereco(propostaInfosBasicas.getEndereco());
                    PropostaInfosBasicas updated = repository.save(record);
                    return ResponseEntity.ok().body(updated);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<Object> deletar(@PathVariable long id) {
        return repository.findById(id)
                .map(record -> {
                    repository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

}
