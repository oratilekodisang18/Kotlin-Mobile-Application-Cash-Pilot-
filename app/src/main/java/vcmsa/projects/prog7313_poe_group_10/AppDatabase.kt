package vcmsa.projects.prog7313_poe_group_10

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CategoryEntity::class, ExpenseEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun expenseDao(): ExpenseDao
}
