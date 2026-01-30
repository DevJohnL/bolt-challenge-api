package br.com.devjhonl.boltchallenge.repository

import br.com.devjhonl.boltchallenge.model.GeradorEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface GeradorRepository : JpaRepository<GeradorEntity, String> {

    fun findTop5ByOrderByPotenciaOutorgadaKwDesc(): List<GeradorEntity>

}