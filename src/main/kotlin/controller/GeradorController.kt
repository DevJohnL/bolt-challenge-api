package br.com.devjhonl.boltchallenge.controller

import br.com.devjhonl.boltchallenge.dto.GeradorResponseDTO
import br.com.devjhonl.boltchallenge.service.GeradorService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/geradores")
@CrossOrigin(origins = ["http://localhost:4200"])
class GeradorController(
    private val service: GeradorService
) {

    @GetMapping("/top5")
    fun getTop5(): ResponseEntity<List<GeradorResponseDTO>> {
        val lista = service.buscarTop5()
        return ResponseEntity.ok(lista)
    }
}