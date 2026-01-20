package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val signUpButton = findViewById<MaterialCardView>(R.id.signUpButton)


        signUpButton.setOnClickListener {
            startActivity(Intent(this, RegistrationActivity::class.java))
        }


    }
}
