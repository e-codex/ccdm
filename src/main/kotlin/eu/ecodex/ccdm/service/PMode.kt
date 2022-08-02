package eu.ecodex.ccdm.service

import java.time.LocalDateTime

class PMode (
        var name: String = "",
        var creationDate: LocalDateTime = LocalDateTime.now(),
        var createdBy: String = "",
        var version: String = ""
        ) {
}