package br.com.devjhonl.boltchallenge.job

import br.com.devjhonl.boltchallenge.service.SyncService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class CsvImportJob(private val syncService: SyncService) {

    // fixedRate = milissegundos
    @Scheduled(fixedRate = 3600000)
    fun runJob() {
        syncService.syncData()
    }
}