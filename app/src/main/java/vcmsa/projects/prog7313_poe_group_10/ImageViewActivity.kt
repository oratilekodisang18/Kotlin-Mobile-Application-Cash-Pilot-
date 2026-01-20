package vcmsa.projects.prog7313_poe_group_10

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ImageViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_view)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val uriString = intent.getStringExtra("image_uri")

        uriString?.let {
            imageView.setImageURI(Uri.parse(it))
        }
    }
}
