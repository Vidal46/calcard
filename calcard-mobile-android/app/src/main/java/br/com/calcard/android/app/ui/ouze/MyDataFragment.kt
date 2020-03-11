package br.com.calcard.android.app.ui.ouze

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import br.com.calcard.android.app.MyApplication
import br.com.calcard.android.app.R
import br.com.calcard.android.app.ui.cards.ProfileViewModel
import br.com.calcard.android.app.utils.Constants
import kotlinx.android.synthetic.main.fragment_my_data.*
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.util.*

class MyDataFragment : Fragment() {

    private lateinit var link: TextView

    private val REQUEST_CAMERA_PERMISSION = 200

    private var change = false

    lateinit var avatar: String

    private lateinit var inputText: EditText

    private val photoObserver = MutableLiveData<Boolean>()

    private val profileViewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_my_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!MyApplication.preferences.getString("avatar", "")!!.equals(null) && !MyApplication.preferences.getString("avatar", "")!!.equals("null")) {
            setLocalAvatar(MyApplication.preferences.getString("avatar", "")!!)
            profileViewModel.avatarLiveData.value = MyApplication.preferences.getString("avatar", "")!!
        }


        bindView()
        observeButton()
        setObservers()
        setClickListener()
    }

    private fun observeButton() {
        inputText.addTextChangedListener(CodeDigitTextWatcher(R.layout.fragment_my_data, my_data_input))
    }

    private fun bindView() {
        link = my_data_sub_title

        inputText = my_data_input_value
    }

    private fun setObservers() {
        profileViewModel.also {
            it.success.observe(this, androidx.lifecycle.Observer {
                dismissProgress()
                Toast.makeText(activity, "Dados atualizados com sucesso", Toast.LENGTH_LONG).show()
                mutableFlag.value = true
                setAvatar()
            })

            it.fail.observe(this, androidx.lifecycle.Observer {
                dismissProgress()
                Toast.makeText(activity, "fail", Toast.LENGTH_LONG).show()
            })

            it.error.observe(this, androidx.lifecycle.Observer {
                dismissProgress()
                Toast.makeText(activity, it, Toast.LENGTH_LONG).show()
            })

            inputText.text.clear()

        }

        photoObserver.observe(this, androidx.lifecycle.Observer {
            changeButtonVisibility()
        })
    }

    private fun setClickListener() {
        link.setOnClickListener {
            AlertDialog.Builder(activity!!)
                    .setTitle("Alterar foto de perfil")
                    .setMessage("Escolha uma imagem")
                    .setPositiveButton("Camera"
                    ) { dialogInterface, i ->
                        if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(activity!!, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_CAMERA_PERMISSION)
                            return@setPositiveButton
                        }
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(takePictureIntent, 1)

                    }
                    .show()

        }
    }

    private fun setAvatar() {
        val decodedString = Base64.decode(profileViewModel.avatarLiveData.value, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        my_data_image.setImageBitmap(decodedByte)
        photoObserver.value = true
    }


    private fun setAvatarExtra(input: String) {
        val decodedString = Base64.decode(input, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        my_data_image.setImageBitmap(decodedByte)
        photoObserver.value = true
    }

    private fun setLocalAvatar(input: String) {
        val decodedString = Base64.decode(input, Base64.DEFAULT)
        val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        my_data_image.setImageBitmap(decodedByte)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(activity, "O acesso à câmera necessita de permissão", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            profileViewModel.avatarLiveData.value = base64FromUri(data!!.data!!, context!!)
            setAvatarExtra(base64FromUri(data!!.data!!, context!!))

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            profileViewModel.avatarLiveData.value = base64FromExtras(data!!.extras!!)
            setAvatarExtra(base64FromExtras(data!!.extras!!))

        }

    }

    private fun base64FromExtras(extras: Bundle): String {
        var image: Bitmap = Objects.requireNonNull(extras).get("data") as Bitmap
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)

    }

    fun onClickSave() {
        showProgress()

        if (inputText.text.toString().equals("")) {
            profileViewModel.nameLiveData.value = MyApplication.preferences.getString("name", "")
        }

        if (profileViewModel.avatarLiveData.value == null) {
            profileViewModel.avatarLiveData.value = MyApplication.preferences.getString("avatar", "")
        }

        profileViewModel.updateAvatar(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null))
    }

    private fun showProgress() {
        my_data_progress.visibility = View.VISIBLE
    }

    private fun dismissProgress() {
        my_data_progress.visibility = View.GONE
    }

    fun base64FromUri(selectedImageUri: Uri, context: Context): String {
        var imageProfile: Bitmap? = null
        try {
            imageProfile = BitmapFactory.decodeStream(Objects.requireNonNull(context.contentResolver.openInputStream(selectedImageUri)))

        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        val baos = ByteArrayOutputStream()
        imageProfile!!.compress(Bitmap.CompressFormat.PNG, 50, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
    }

    private fun compareValues() {
        if (!inputText.text.toString().equals(MyApplication.preferences.getString("name", null))) {
            profileViewModel.nameLiveData.value = inputText.text.toString()
        }
    }

    private fun changeButtonVisibility() {
        my_data_button.setBackgroundResource(R.drawable.btn_orange)
        my_data_button.setOnClickListener {

            compareValues()
            onClickSave()
        }
    }

    companion object {
        @JvmStatic
        lateinit var mutableFlag: MutableLiveData<Boolean>
    }

    inner class CodeDigitTextWatcher(private val parent: Int,
                                     private val view: View
    ) : TextWatcher {
        override fun afterTextChanged(value: Editable?) {
            when {
                inputText.text!!.isNotEmpty() -> {
                    changeButtonVisibility()
                }

                inputText.text!!.isEmpty() -> {
                    my_data_button.setBackgroundResource(R.drawable.btn_disable_square)
                    my_data_button.setOnClickListener {
                        Toast.makeText(activity, "Não", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

    }
}
