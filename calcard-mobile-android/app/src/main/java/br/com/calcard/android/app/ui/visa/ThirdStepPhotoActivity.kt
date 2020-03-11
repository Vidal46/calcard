package br.com.calcard.android.app.ui.visa

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.Settings
import android.util.Base64
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.activity_third_step_photo.*
import java.io.ByteArrayOutputStream


class ThirdStepPhotoActivity : AppCompatActivity() {

    lateinit var bitmap: Bitmap
    val viewModel = VisaMigrationViewModel()
    var extraObj = MigrationElegible()
    var cpfIntent = String()
    var deviceId = String()
    lateinit var bitImage: String

    @SuppressLint("HardwareIds")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_step_photo)

        intent.run {
            bitmap = BitmapFactory.decodeByteArray(
                    getByteArrayExtra("picture"), 0, getByteArrayExtra("picture").size)
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
            cpfIntent = getStringExtra("cpf")
        }

        deviceId = Settings.Secure.getString(this.contentResolver,
                Settings.Secure.ANDROID_ID)

        bitmap = Bitmap.createScaledBitmap(bitmap, 480, 640, false)
        val baos = ByteArrayOutputStream()

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        bitImage = Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)


        picture_result.setImageBitmap(bitmap)
        setObservers()
        setClickListeners()
    }

    private fun showAnalyzesViews() {
        visa_third_step_photo_progress.visibility = View.VISIBLE
        visa_third_step_photo_progress_text.visibility = View.VISIBLE
    }

    private fun dismissAnalyzesView() {
        visa_third_step_photo_progress.visibility = View.GONE
        visa_third_step_photo_progress_text.visibility = View.GONE
    }

    private fun setClickListeners() {
        visa_third_step_photo_no_button.setOnClickListener {
            val cameraIntent = Intent(this, CameraActivity::class.java)
            cameraIntent.putExtra("cpf", cpfIntent)
            cameraIntent.putExtra("migrationEdible", extraObj)
            startActivity(cameraIntent)
            finish()
        }

        visa_third_step_photo_yes_button.setOnClickListener {
            checkPicture(cpfIntent, bitImage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun setObservers() {
        viewModel.connectionError.observe(this, Observer {
            dismissAnalyzesView()
            Toast.makeText(this, "Erro de conexão", Toast.LENGTH_SHORT).show()
        })

        viewModel.codeOk.observe(this, Observer {
            intent = Intent(this, ApprovedCreditActivity::class.java)
            intent.putExtra("migrationEdible", extraObj)
            intent.putExtra("cpf", cpfIntent)
            dismissAnalyzesView()
            startActivity(intent)
        })

        viewModel.errorHandler.observe(this, Observer {
            showToast(it)
            dismissAnalyzesView()
            intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("migrationEdible", extraObj)
            intent.putExtra("cpf", cpfIntent)
            startActivity(intent)
        })
    }

    private fun checkPicture(input: String, pic: String) {
        showAnalyzesViews()
        viewModel.sendPicture(Constants.BEARER_API, pic, input, deviceId)
    }
}
