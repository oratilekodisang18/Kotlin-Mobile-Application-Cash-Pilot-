package vcmsa.projects.prog7313_poe_group_10

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CategoryDao {

    @Insert
    suspend fun insertCategory(category: CategoryEntity)

    @Insert
    suspend fun insertCategories(categories: List<CategoryEntity>) // NEW for multiple inserts

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<CategoryEntity>
}
