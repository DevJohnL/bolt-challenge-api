package br.com.devjhonl.boltchallenge.model

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Column
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "geradores")
data class GeradorEntity(

    @Id
    @Column(name = "codigo_ceg")
    var codigoCeg: String = "",

    @Column(name = "nome_empreendimento")
    var nomeEmpreendimento: String = "",

    @Column(name = "tipo_geracao")
    var tipoGeracao: String = "",


    @Column(name = "potencia_outorgada_kw")
    var potenciaOutorgadaKw: BigDecimal = BigDecimal.ZERO

)