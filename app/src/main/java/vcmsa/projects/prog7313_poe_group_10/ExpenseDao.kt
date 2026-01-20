package vcmsa.projects.prog7313_poe_group_10

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {

    @Insert
    suspend fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT * FROM expenses")
    suspend fun getAllExpenses(): List<ExpenseEntity>
}
