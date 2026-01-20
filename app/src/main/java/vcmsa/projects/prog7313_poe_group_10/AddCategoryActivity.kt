package vcmsa.projects.prog7313_poe_group_10

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch
import vcmsa.projects.prog7313_poe_group_10.databinding.ActivityAddCategoryBinding

class AddCategoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCategoryBinding
    private lateinit var db: AppDatabase

    private var selectedType: String = "Expense" // Default to Expense

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Room database
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "budget-tracker-db"
        ).build()

        // Set initial selection
        highlightSelectedButton(binding.btnExpense, binding.btnIncome)

        // Expense button click
        binding.btnExpense.setOnClickListener {
            selectedType = "Expense"
            highlightSelectedButton(binding.btnExpense, binding.btnIncome)
        }

        // Income button click
        binding.btnIncome.setOnClickListener {
            selectedType = "Income"
            highlightSelectedButton(binding.btnIncome, binding.btnExpense)
        }

        // Create Category button click
        binding.btnCreateCategory.setOnClickListener {
            val categoryName = binding.etCategoryName.text.toString().trim()
            val monthlyLimitText = binding.etMonthlyLimit.text.toString().trim()
            val monthlyLimit = if (monthlyLimitText.isNotEmpty()) monthlyLimitText.toDoubleOrNull() else null

            if (categoryName.isNotEmpty()) {
                val newCategory = CategoryEntity(
                    name = categoryName,
                    type = selectedType,
                    monthlyLimit = monthlyLimit
                )

                lifecycleScope.launch {
                    db.categoryDao().insertCategory(newCategory)
                    finish() // Close screen after saving
                }
            }
        }

        // Back button click
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    private fun highlightSelectedButton(selected: MaterialButton, other: MaterialButton) {
        selected.strokeWidth = 4 // Thicker black outline
        selected.strokeColor = ContextCompat.getColorStateList(this, android.R.color.black)

        other.strokeWidth = 0 // No border on the other button
    }
}
