package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.prog7313_poe_group_10.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Use the same SharedPreferences as RegistrationActivity
        sharedPreferences = getSharedPreferences(RegistrationActivity.PREF_NAME, MODE_PRIVATE)

        // Set up login button click listener
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (validateInput(email, password)) {
                val savedEmail = sharedPreferences.getString(RegistrationActivity.KEY_USER_EMAIL, "")
                val savedPassword = sharedPreferences.getString(RegistrationActivity.KEY_USER_PASSWORD, "")

                if (email == savedEmail && password == savedPassword) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, BudgetGoalSettingActivity::class.java)
                    intent.putExtra("EMAIL", email)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.forgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }

        binding.createAccount.setOnClickListener {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }
}
