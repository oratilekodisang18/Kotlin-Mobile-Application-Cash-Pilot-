package vcmsa.projects.prog7313_poe_group_10

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

/**
 * Registration Activity that handles user sign-up functionality.
 * Stores user credentials in SharedPreferences for login page to access.
 */
class RegistrationActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etConfirmPassword: EditText
    private lateinit var btnSignUp: MaterialCardView
    private lateinit var tvLoginPrompt: TextView
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREF_NAME = "CashPilotPrefs"
        const val KEY_USER_EMAIL = "user_email"
        const val KEY_USER_PASSWORD = "user_password"
        const val KEY_IS_REGISTERED = "is_registered"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        // Check if user is already registered
        if (sharedPreferences.getBoolean(KEY_IS_REGISTERED, false)) {
            // If already registered, you might want to redirect to login
            Toast.makeText(this, "User already registered. Please log in.", Toast.LENGTH_SHORT).show()
            // You could navigate to login here if required
        }

        // Initialize views
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnSignUp = findViewById(R.id.btnSignUp)
        tvLoginPrompt = findViewById(R.id.tvLoginPrompt)

        // Set up click listener for sign up button
        btnSignUp.setOnClickListener {
            if (validateRegistrationInputs()) {
                saveUserCredentials()
                handleSuccessfulRegistration()
            }
        }

        //Set up login prompt click navigation
        tvLoginPrompt.setOnClickListener {
        navigateToLogin()
        }
    }

    /**
     * Validates registration form inputs
     * @return true if all inputs are valid, false otherwise
     */
    private fun validateRegistrationInputs(): Boolean {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()
        val confirmPassword = etConfirmPassword.text.toString()

        // Validate email
        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            return false
        } else if (!isValidEmail(email)) {
            etEmail.error = "Enter a valid email address"
            return false
        }

        // Validate password
        if (password.isEmpty()) {
            etPassword.error = "Password is required"
            return false
        } else if (password.length < 6) {
            etPassword.error = "Password must be at least 6 characters"
            return false
        }

        // Validate password confirmation
        if (confirmPassword.isEmpty()) {
            etConfirmPassword.error = "Please confirm your password"
            return false
        } else if (password != confirmPassword) {
            etConfirmPassword.error = "Passwords don't match"
            return false
        }

        return true
    }

    /**
     * Checks if email is in valid format
     */
    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Saves user credentials to SharedPreferences
     */
    private fun saveUserCredentials() {
        val email = etEmail.text.toString().trim()
        val password = etPassword.text.toString()

        // Save to SharedPreferences
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USER_EMAIL, email)
        editor.putString(KEY_USER_PASSWORD, password)
        editor.putBoolean(KEY_IS_REGISTERED, true)
        editor.apply()

        // Log for debugging
        println("User credentials saved: Email=$email, Password=$password")
    }

    /**
     * Handles successful registration
     */
    private fun handleSuccessfulRegistration() {
        // Show success message
        Toast.makeText(this, "Registration successful! You can now log in.", Toast.LENGTH_SHORT).show()

        // Navigate to login activity instead of main activity
        navigateToLogin()
    }

    //Navigates to the login activity
    private fun navigateToLogin() {
    val intent = Intent(this, LoginActivity::class.java)
    startActivity(intent)
    finish()
    }
}