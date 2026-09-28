package com.civicreportgh.app

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

/**
 * The categories of civic issues this app supports.
 */
enum class IssueCategory(val displayName: String) {
    ROAD("Road / pothole damage"),
    WATER_SUPPLY("Water supply issue"),
    ELECTRICITY("Electricity / streetlight fault"),
    SANITATION("Waste / sanitation issue"),
    ENVIRONMENT("Environmental hazard / illegal dumping"),
    SAFETY("Public safety concern"),
    OTHER("Other / not sure")
}

/**
 * The institution a report for a given category should be routed to.
 */
@Serializable
data class Institution(
    val name: String,
    val email: String,
    val verifiedContact: Boolean
)

object InstitutionRepository {

    private val defaultMapping: Map<IssueCategory, Institution> = mapOf(
        IssueCategory.ROAD to Institution(
            name = "Ghana Highway Authority",
            email = "info@gha.gov.gh",
            verifiedContact = false
        ),
        IssueCategory.WATER_SUPPLY to Institution(
            name = "Ghana Water Limited (GWCL)",
            email = "info@gwcl.com.gh",
            verifiedContact = true
        ),
        IssueCategory.ELECTRICITY to Institution(
            name = "Electricity Company of Ghana (ECG)",
            email = "help@ecggh.com",
            verifiedContact = true
        ),
        IssueCategory.SANITATION to Institution(
            name = "Your Metropolitan/Municipal/District Assembly (MMDA)",
            email = "",
            verifiedContact = false
        ),
        IssueCategory.ENVIRONMENT to Institution(
            name = "Environmental Protection Agency (EPA)",
            email = "info@epa.gov.gh",
            verifiedContact = true
        ),
        IssueCategory.SAFETY to Institution(
            name = "Ghana Police Service",
            email = "info@police.gov.gh",
            verifiedContact = false
        ),
        IssueCategory.OTHER to Institution(
            name = "Your Metropolitan/Municipal/District Assembly (MMDA)",
            email = "",
            verifiedContact = false
        )
    )

    private var remoteMapping: Map<IssueCategory, Institution>? = null

    fun updateFromRemote(json: String) {
        try {
            val parsed = Json.decodeFromString<Map<String, Institution>>(json)
            // Map string keys back to enum keys
            remoteMapping = parsed.mapKeys { (key, _) -> 
                runCatching { IssueCategory.valueOf(key) }.getOrDefault(IssueCategory.OTHER)
            }
        } catch (e: Exception) {
            // Fallback to default if parsing fails
            remoteMapping = null
        }
    }

    fun institutionFor(category: IssueCategory): Institution =
        remoteMapping?.get(category) ?: defaultMapping[category] ?: defaultMapping.getValue(IssueCategory.OTHER)

    /**
     * Very small keyword heuristic that turns an ML Kit image label
     * (e.g. "Road", "Water", "Trash") into one of our issue categories.
     */
    fun categoryFromLabel(label: String): IssueCategory {
        val l = label.lowercase()
        return when {
            listOf("road", "asphalt", "pothole", "highway", "street").any { l.contains(it) } -> IssueCategory.ROAD
            listOf("water", "pipe", "flood", "puddle").any { l.contains(it) } -> IssueCategory.WATER_SUPPLY
            listOf("pole", "wire", "electric", "cable", "light").any { l.contains(it) } -> IssueCategory.ELECTRICITY
            listOf("trash", "garbage", "waste", "rubbish", "landfill", "dump").any { l.contains(it) } -> IssueCategory.SANITATION
            listOf("smoke", "pollution", "fire", "chemical").any { l.contains(it) } -> IssueCategory.ENVIRONMENT
            else -> IssueCategory.OTHER
        }
    }
}
