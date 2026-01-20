package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe_group_10.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var db: AppDatabase

    // Bottom Navigation
    private lateinit var bottomNavigationView : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Bottom navigation Logic
        bottomNavigationView = findViewById(R.id.bottom_navigation)
        // Load a default fragment
        loadFragment(HomeFragment())

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_categories -> CategoriesFragment()
                R.id.nav_charts -> ChartsFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            loadFragment(fragment)
            true
        }

        // Initialize database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "budget-tracker-db"
        ).build()

        // Prepopulate default categories
        prepopulateCategories()
/*
        // Add Category button
        binding.btnAddCategory.setOnClickListener {
            val intent = Intent(this, AddCategoryActivity::class.java)
            startActivity(intent)
        }


        binding.btnAddExpense.setOnClickListener {
            val intent = Intent(this, AddExpenseActivity::class.java)
            startActivity(intent)
        }

 */
    }

    private fun prepopulateCategories() {
        lifecycleScope.launch {
            val existingCategories = db.categoryDao().getAllCategories()
            if (existingCategories.isEmpty()) {
                val defaultCategories = listOf(
                    CategoryEntity(name = "Food", type = "Expense", monthlyLimit = null),
                    CategoryEntity(name = "Transport", type = "Expense", monthlyLimit = null),
                    CategoryEntity(name = "Health", type = "Expense", monthlyLimit = null),
                    CategoryEntity(name = "Entertainment", type = "Expense", monthlyLimit = null),
                    CategoryEntity(name = "Salary", type = "Income", monthlyLimit = null)
                )
                db.categoryDao().insertCategories(defaultCategories)
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}
