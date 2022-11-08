package hu.tsukiakari.xiv.characterDetails

import hu.tsukiakari.xiv.network.model.lodestone.LodestoneCharacter

interface CharacterDataHolder {
    fun getCharacterData(): LodestoneCharacter?
}