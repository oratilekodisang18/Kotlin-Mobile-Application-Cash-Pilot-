package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class BudgetGoalSettingActivity : AppCompatActivity() {


    private lateinit var etMinGoal: EditText
    private lateinit var etMaxGoal: EditText
    private lateinit var tvMinGoalDisplay: TextView
    private lateinit var tvMaxGoalDisplay: TextView
    private lateinit var tilMinGoal: TextInputLayout
    private lateinit var tilMaxGoal: TextInputLayout
    private lateinit var cardSaveButton: CardView

    private val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA")).apply {
        currency = Currency.getInstance("ZAR")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_budget_goal_setting)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initializeViews()
        setupInputListeners()
        setupSaveButton()
        loadExistingGoals()
    }

    private fun initializeViews() {
        etMinGoal = findViewById(R.id.etMinGoal)
        etMaxGoal = findViewById(R.id.etMaxGoal)
        tvMinGoalDisplay = findViewById(R.id.tvMinGoalDisplay)
        tvMaxGoalDisplay = findViewById(R.id.tvMaxGoalDisplay)
        tilMinGoal = findViewById(R.id.tilMinGoal)
        tilMaxGoal = findViewById(R.id.tilMaxGoal)
        cardSaveButton = findViewById(R.id.cardSaveButton)
    }


    private fun setupInputListeners() {
        // Min Goal input formatting
        etMinGoal.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty() && !text.startsWith("R")) {
                try {
                    val value = text.toString().filter { it.isDigit() || it == '.' }
                    val amount = value.toDoubleOrNull() ?: 0.0
                    tvMinGoalDisplay.text = currencyFormatter.format(amount)
                } catch (e: Exception) {
                    tvMinGoalDisplay.text = currencyFormatter.format(0)
                }
            } else {
                tvMinGoalDisplay.text = currencyFormatter.format(0)
            }
        }

        // Max Goal input formatting
        etMaxGoal.doOnTextChanged { text, _, _, _ ->
            if (!text.isNullOrEmpty() && !text.startsWith("R")) {
                try {
                    val value = text.toString().filter { it.isDigit() || it == '.' }
                    val amount = value.toDoubleOrNull() ?: 0.0
                    tvMaxGoalDisplay.text = currencyFormatter.format(amount)
                } catch (e: Exception) {
                    tvMaxGoalDisplay.text = currencyFormatter.format(0)
                }
            } else {
                tvMaxGoalDisplay.text = currencyFormatter.format(0)
            }
        }
    }

    private fun setupSaveButton() {
        cardSaveButton.setOnClickListener {
            if (validateInputs()) {
                saveGoals()
                Snackbar.make(
                    findViewById(android.R.id.content),
                    "Budget goals saved successfully",
                    Snackbar.LENGTH_SHORT
                ).show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()

            }
        }
    }

    private fun validateInputs(): Boolean {
        val minGoalText = etMinGoal.text.toString()
        val maxGoalText = etMaxGoal.text.toString()

        if (minGoalText.isEmpty()) {
            tilMinGoal.error = "Please enter a minimum goal"
            return false
        } else {
            tilMinGoal.error = null
        }

        if (maxGoalText.isEmpty()) {
            tilMaxGoal.error = "Please enter a maximum goal"
            return false
        } else {
            tilMaxGoal.error = null
        }

        val minGoal = minGoalText.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
        val maxGoal = maxGoalText.filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0

        if (minGoal >= maxGoal) {
            tilMaxGoal.error = "Maximum goal must be greater than minimum goal"
            return false
        }

        return true
    }

    private fun saveGoals() {
        val minGoal =
            etMinGoal.text.toString().filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0
        val maxGoal =
            etMaxGoal.text.toString().filter { it.isDigit() || it == '.' }.toDoubleOrNull() ?: 0.0

        // Save to SharedPreferences
        val sharedPrefs = getSharedPreferences("budget_prefs", MODE_PRIVATE)
        with(sharedPrefs.edit()) {
            putFloat("min_monthly_goal", minGoal.toFloat())
            putFloat("max_monthly_goal", maxGoal.toFloat())
            apply()
        }

        // Here you would also save to your database if you're using one
    }

    private fun loadExistingGoals() {
        val sharedPrefs = getSharedPreferences("budget_prefs", MODE_PRIVATE)
        val minGoal = sharedPrefs.getFloat("min_monthly_goal", 0f)
        val maxGoal = sharedPrefs.getFloat("max_monthly_goal", 0f)

        if (minGoal > 0) {
            etMinGoal.setText(minGoal.toString())
            tvMinGoalDisplay.text = currencyFormatter.format(minGoal)
        }

        if (maxGoal > 0) {
            etMaxGoal.setText(maxGoal.toString())
            tvMaxGoalDisplay.text = currencyFormatter.format(maxGoal)
        }
    }
}

