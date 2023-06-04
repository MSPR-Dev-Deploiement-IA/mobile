package fr.epsi.arosaje

data class Photo(
    val id: Int?,
    val plant_id: Int?,
    val photo_file_url: String,
    val upload_date: String?,
    val description: String?
)