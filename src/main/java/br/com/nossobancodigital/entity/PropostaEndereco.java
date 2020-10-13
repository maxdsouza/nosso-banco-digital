package br.com.nossobancodigital.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class PropostaEndereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(message = "CEP com formato incorreto.", regexp = "\\d{5}-\\d{3}")
    @NotNull(message = "CEP é obrigatório.")
    @Column(nullable = false)
    private String cep;

    @NotEmpty(message = "Rua é obrigatório.")
    @Column(nullable = false)
    private String rua;

    @NotEmpty(message = "Bairro é obrigatório.")
    @Column(nullable = false)
    private String bairro;

    @NotEmpty(message = "Complemento é obrigatório.")
    @Column(nullable = false)
    private String complemento;

    @NotEmpty(message = "Cidade é obrigatória.")
    @Column(nullable = false)
    private String cidade;

    @NotEmpty(message = "Estado é obrigatório.")
    @Column(nullable = false)
    private String estado;

    @OneToOne
    @JoinTable(name = "propostaib_propostaend",
            joinColumns = @JoinColumn(name = "propostaInfosBasicas_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "propostaEndereco_id", referencedColumnName = "id"))
    private PropostaInfosBasicas propostaInfosBasicas;
}
