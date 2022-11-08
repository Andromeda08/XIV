package hu.tsukiakari.xiv.characterDetails.model

import hu.tsukiakari.xiv.network.model.lodestone.details.Job

data class JobExtended(
    val job: Job,
    val resID: Int
)
