package br.com.devjhonl.boltchallenge.service

import br.com.devjhonl.boltchallenge.dto.GeradorResponseDTO
import br.com.devjhonl.boltchallenge.repository.GeradorRepository
import org.springframework.stereotype.Service

@Service
class GeradorService(
    private val repository: GeradorRepository
) {

    fun buscarTop5(): List<GeradorResponseDTO> {

        val entidades = repository.findTop5ByOrderByPotenciaOutorgadaKwDesc()

        return entidades.map { entidade ->
            GeradorResponseDTO(
                nome = entidade.nomeEmpreendimento,
                codigo = entidade.codigoCeg,
                tipo = entidade.tipoGeracao,
                potencia = entidade.potenciaOutorgadaKw
            )
        }
    }
}