package br.com.devjhonl.boltchallenge.service

import br.com.devjhonl.boltchallenge.model.GeradorEntity
import br.com.devjhonl.boltchallenge.repository.GeradorRepository
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets
import org.springframework.core.io.Resource

@Service
class SyncService(
    private val repository: GeradorRepository,
    private val restClient: RestClient.Builder
) {

    private val logger = LoggerFactory.getLogger(SyncService::class.java)

    private val URL_ANEEL = "https://dadosabertos.aneel.gov.br/dataset/57e4b8b5-a5db-40e6-9901-27ca629d0477/resource/4a615df8-4c25-48fa-bbea-873a36a79518/download/ralie-usina.csv5"

    fun syncData() {
        logger.info("Iniciando sincronização com ANEEL...")

        repository.deleteAll()

        try {
            val responseResource = restClient.build()
                .get()
                .uri(URL_ANEEL)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .retrieve()
                .body(Resource::class.java)

            val inputStream = responseResource?.inputStream

            if (inputStream == null) {
                logger.error("Falha ao baixar arquivo: InputStream vazio")
                return
            }



            val reader = InputStreamReader(inputStream, java.nio.charset.StandardCharsets.ISO_8859_1)

            val csvParser = CSVParser(reader, CSVFormat.DEFAULT
                .builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setDelimiter(';')
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build()
            )

            val entidadesParaSalvar = mutableListOf<GeradorEntity>()

            for (csvRecord in csvParser) {
                val entidade = GeradorEntity(
                    codigoCeg = csvRecord.get("CodCEG"),
                    nomeEmpreendimento = csvRecord.get("NomEmpreendimento"),
                    tipoGeracao = csvRecord.get("SigTipoGeracao"),

                    potenciaOutorgadaKw = csvRecord.get("MdaPotenciaOutorgadaKw")
                        .replace(",", ".")
                        .toBigDecimalOrNull() ?: java.math.BigDecimal.ZERO
                )

                entidadesParaSalvar.add(entidade)

                if (entidadesParaSalvar.size >= 1000) {
                    repository.saveAll(entidadesParaSalvar)
                    entidadesParaSalvar.clear()
                    logger.info("Lote de 1000 registros salvo...")
                }
            }

            if (entidadesParaSalvar.isNotEmpty()) {
                repository.saveAll(entidadesParaSalvar)
            }

            logger.info("Sincronização finalizada com sucesso!")
            csvParser.close()

        } catch (e: Exception) {
            logger.error("Erro ao sincronizar dados", e)
        }
    }
}