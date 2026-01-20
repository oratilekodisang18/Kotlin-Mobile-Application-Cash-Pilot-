package vcmsa.projects.prog7313_poe_group_10

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
    val startTime: String,
    val endTime: String?,
    val amount: Double,
    val name: String,
    val description: String?,
    val category: String,
    val photoUri: String?
)
