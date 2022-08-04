package eu.ecodex.ccdm.service.cmtclient

import java.time.LocalDateTime

class PModeDTO (
        var name: String = "",
        var creationDate: LocalDateTime = LocalDateTime.now(),
        var createdBy: String = "",
        var version: String = ""
        ) {
}