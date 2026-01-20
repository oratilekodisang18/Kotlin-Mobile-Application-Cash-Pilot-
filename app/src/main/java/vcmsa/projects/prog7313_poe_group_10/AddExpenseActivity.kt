package vcmsa.projects.prog7313_poe_group_10

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.button.MaterialButton
import vcmsa.projects.prog7313_poe_group_10.databinding.ActivityAddExpenseBinding
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var db: AppDatabase
    private val calendar = Calendar.getInstance()
    private var photoUri: Uri? = null

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri
            Toast.makeText(this, "Photo selected!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "No photo selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "budget-tracker-db"
        ).build()

        setupDatePicker()
        setupExpenseIncomeToggle()
        setupCategorySpinner()
        setupPickPhoto()
        setupCreateButton()
        setupBackButton()
    }

    private fun setupDatePicker() {
        updateDateInView()
        binding.btnPickDate.setOnClickListener {
            val datePicker = DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    updateDateInView()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }
        binding.btnResetDate.setOnClickListener {
            calendar.time = Date()
            updateDateInView()
        }
    }

    private fun updateDateInView() {
        val format = SimpleDateFormat("EEE dd MMM yy", Locale.getDefault())
        binding.txtSelectedDate.text = format.format(calendar.time)
    }

    private fun setupExpenseIncomeToggle() {
        binding.btnExpense.isSelected = true

        binding.btnExpense.setOnClickListener {
            binding.btnExpense.isSelected = true
            binding.btnIncome.isSelected = false
            setButtonStyles(binding.btnExpense, binding.btnIncome)
        }

        binding.btnIncome.setOnClickListener {
            binding.btnExpense.isSelected = false
            binding.btnIncome.isSelected = true
            setButtonStyles(binding.btnIncome, binding.btnExpense)
        }
    }

    private fun setButtonStyles(selected: MaterialButton, unselected: MaterialButton) {
        selected.setBackgroundColor(getColor(R.color.expense_red))
        selected.setTextColor(getColor(R.color.white))
        unselected.setBackgroundColor(getColor(R.color.input_border))
        unselected.setTextColor(getColor(R.color.white))
    }

    private fun setupCategorySpinner() {
        lifecycleScope.launch {
            val categories = db.categoryDao().getAllCategories().map { it.name }.toMutableList()
            categories.add(0, "Choose a category")

            val adapter = ArrayAdapter(this@AddExpenseActivity, android.R.layout.simple_spinner_item, categories)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerCategory.adapter = adapter
        }
    }

    private fun setupPickPhoto() {
        binding.btnPickPhoto.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }
    }

    private fun setupCreateButton() {
        binding.btnCreate.setOnClickListener {
            val amountStr = binding.etAmount.text.toString()
            val name = binding.etName.text.toString()
            val description = binding.etDescription.text.toString()
            val selectedCategory = binding.spinnerCategory.selectedItem.toString()

            if (amountStr.isEmpty() || name.isEmpty() || selectedCategory == "Choose a category") {
                Toast.makeText(this, "Please complete all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = ExpenseEntity(
                date = binding.txtSelectedDate.text.toString(),
                startTime = "",  // not used in this case
                endTime = null,
                amount = amountStr.toDouble(),
                name = name,
                description = if (description.isEmpty()) null else description,
                category = selectedCategory,
                photoUri = photoUri?.toString()
            )

            lifecycleScope.launch {
                db.expenseDao().insertExpense(expense)
                runOnUiThread {
                    Toast.makeText(this@AddExpenseActivity, "Transaction saved!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}