package br.com.calcard.android.app.ui.visa

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import br.com.calcard.android.app.R
import br.com.calcard.android.app.model.MigrationElegible
import br.com.calcard.android.app.ui.redirection.MainRedirectionActivity
import kotlinx.android.synthetic.main.activity_first_step_photo.*

class FirstStepPhotoActivity : AppCompatActivity() {
    var extraObj = MigrationElegible()
    var cpfIntent = String()
    val CAMERA_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_step_photo)

        intent.run {
            extraObj = getSerializableExtra("migrationEdible") as MigrationElegible
            cpfIntent = getStringExtra("cpf")
        }

        visa_first_step_photo_button.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            intent.putExtra("migrationEdible", extraObj)
            intent.putExtra("cpf", cpfIntent)
            startActivity(intent)
        }
        requestStoragePermission()

    }

    override fun onBackPressed() {
        onBackPressedActivity()
    }

    private fun onBackPressedActivity() {
        startActivity(Intent(this, MainRedirectionActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {

            AlertDialog.Builder(this)
                    .setTitle("Permitir que o app ouze tire fotos?")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok") { dialog, which ->
                        ActivityCompat.requestPermissions(this@FirstStepPhotoActivity,
                                arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
                    }
                    .setNegativeButton("cancel") { dialog, which -> dialog.dismiss() }
                    .create().show()

        } else {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
        }
    }

}

