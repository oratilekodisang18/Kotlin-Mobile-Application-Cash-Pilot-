package vcmsa.projects.prog7313_poe_group_10

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val type: String,
    val monthlyLimit: Double?
)
