package br.com.calcard.android.app.ui.visa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity
import kotlinx.android.synthetic.main.activity_limit_refusement.*

class LimitRefusementActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limit_refusement)

        ops_no_limite_yes_button.setOnClickListener {
            intent  = Intent(this, MainRedirectionActivity::class.java)
            startActivity(intent)
        }

        ops_no_limite_no_button.setOnClickListener {
            finish()
        }
    }
}
