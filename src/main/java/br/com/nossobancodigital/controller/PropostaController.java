package br.com.nossobancodigital.controller;

import br.com.nossobancodigital.entity.PropostaEndereco;
import br.com.nossobancodigital.entity.PropostaInfosBasicas;
import br.com.nossobancodigital.repository.PropostaEnderecoRepository;
import br.com.nossobancodigital.repository.PropostaInfosBasicasRepository;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
@RequestMapping(path = "/proposta")
public class PropostaController {

    private final PropostaInfosBasicasRepository propostaInfosBasicasRepository;
    private final PropostaEnderecoRepository propostaEnderecoRepository;
    private List<URI> uriList = new ArrayList<>();

    PropostaController(PropostaInfosBasicasRepository propostaInfosBasicasRepository,
                       PropostaEnderecoRepository propostaEnderecoRepository) {
        this.propostaInfosBasicasRepository = propostaInfosBasicasRepository;
        this.propostaEnderecoRepository = propostaEnderecoRepository;
    }

    @PostMapping(path = "/informacoes-basicas")
    public ResponseEntity<PropostaInfosBasicas> salvarInfosBasicas(@Valid @RequestBody PropostaInfosBasicas propostaInfosBasicas) {
        PropostaInfosBasicas propostaInfosBasicasPersistida = propostaInfosBasicasRepository.save(propostaInfosBasicas);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(propostaInfosBasicasPersistida.getId()).toUri();

        uriList.add(location);

        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/informacoes-basicas")
    public List<PropostaInfosBasicas> buscarTodasInfosBasicas() {
        return propostaInfosBasicasRepository.findAll();
    }

    @GetMapping(path = {"/informacoes-basicas/{id}"})
    public ResponseEntity<PropostaInfosBasicas> buscarInfosBasicasPorId(@PathVariable long id) {
        return propostaInfosBasicasRepository.findById(id)
                .map(record -> ResponseEntity.ok().body(record))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/informacoes-basicas/{id}")
    public ResponseEntity<PropostaInfosBasicas> atualizarInfosBasicas(
        @PathVariable("id") long id,
        @RequestBody @Valid PropostaInfosBasicas propostaInfosBasicas
    ) {
        return propostaInfosBasicasRepository.findById(id)
                .map(record -> {
                    record.setNome(propostaInfosBasicas.getNome());
                    record.setSobrenome(propostaInfosBasicas.getSobrenome());
                    record.setEmail(propostaInfosBasicas.getEmail());
                    record.setDataNascimento(propostaInfosBasicas.getDataNascimento());
                    record.setCpf(propostaInfosBasicas.getCpf());
                    PropostaInfosBasicas atualizada = propostaInfosBasicasRepository.save(record);
                    return ResponseEntity.ok().body(atualizada);
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = {"/informacoes-basicas/{id}"})
    public ResponseEntity<Object> deletarInfosBasicas(@PathVariable long id) {
        return propostaInfosBasicasRepository.findById(id)
                .map(record -> {
                    propostaInfosBasicasRepository.deleteById(id);
                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/endereco")
    public ResponseEntity<PropostaEndereco> salvarEndereco(@Valid @RequestBody PropostaEndereco propostaEndereco){

        String caminho = uriList.get(0).getPath();

        Optional<PropostaInfosBasicas> propostaInfosBasicasEncontrada = propostaInfosBasicasRepository.findById(Long.parseLong(caminho.substring(30)));

        propostaEndereco.setPropostaInfosBasicas(propostaInfosBasicasEncontrada.get());
        PropostaEndereco propostaEnderecoPersistida = propostaEnderecoRepository.save(propostaEndereco);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(propostaEnderecoPersistida.getId()).toUri();

        uriList.add(location);

        return ResponseEntity.created(location).build();
    }
}
