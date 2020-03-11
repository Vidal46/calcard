package br.com.calcard.android.app.ui.cards;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.Observable;

import java.util.Objects;

import br.com.calcard.android.app.MyApplication;
import br.com.calcard.android.app.R;
import br.com.calcard.android.app.databinding.ActivityProfileBinding;
import br.com.calcard.android.app.utils.Constants;
import br.com.calcard.android.app.utils.RightDrawableOnTouchListener;

public class ProfileActivity extends AppCompatActivity {

    ProfileViewModel viewModel;
    ActivityProfileBinding binding;
    private ProgressDialog progressDialog;
    private static final int REQUEST_CAMERA_PERMISSION = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setStatusBarGradiant(this);
        viewModel = new ProfileViewModel();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        binding.setViewProfile(viewModel);
        binding.executePendingBindings();
        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
        binding.layout.clearFocus();
        binds();
        binding.getViewProfile().avatar.set(MyApplication.preferences.getString("avatar", null));
        binding.getViewProfile().name.set(MyApplication.preferences.getString("name", null));
        binding.imgicon.setOnClickListener(v -> {
            new AlertDialog.Builder(ProfileActivity.this)
                    .setTitle("Alterar foto de perfil")
                    .setMessage("Escolha uma imagem")
                    .setPositiveButton("Camera",
                            (dialogInterface, i) -> {
                                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(ProfileActivity.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA_PERMISSION);
                                    return;
                                }
                                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePictureIntent, 1);

                            })
                    .setNegativeButton("Galeria",
                            (dialogInterface, i) -> {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), 2);
                            })
                    .show();
        });

    }

    public void onClickSave() {
        binding.layout.clearFocus();
        binding.getViewProfile().updateAvatar(Constants.BEARER_API, MyApplication.preferences.getString("tokenUser", null));
    }

    public void onClickChangePassword(View view) {
        startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
    }

    public void onClickLayout(View view) {
        binding.layout.clearFocus();
    }

    public void binds() {
        binding.getViewProfile().name.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (!binding.getViewProfile().name.get().equals(MyApplication.preferences.getString("name", null))) {
                    binding.edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.salvar, 0);
                    binding.edt.setOnTouchListener(new RightDrawableOnTouchListener(binding.edt) {
                        @Override
                        public boolean onDrawableTouch(final MotionEvent event) {
                            onClickSave();
                            event.setAction(MotionEvent.ACTION_CANCEL);
                            return false;
                        }
                    });
                } else {
                    binding.edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit, 0);
                    binding.edt.setOnTouchListener(null);

                }
            }
        });
        binding.getViewProfile().avatar.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String avatar = binding.getViewProfile().avatar.get();
                if (avatar != null) {
                    byte[] decodedString = Base64.decode(avatar, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    binding.imgicon.setImageBitmap(decodedByte);
                }
            }
        });
        binding.getViewProfile().loadingState.addOnPropertyChangedCallback(
                new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(Observable observable, int i) {
                        boolean isLoading = binding.getViewProfile().loadingState.get();
                        if (isLoading) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                            Objects.requireNonNull(imm).hideSoftInputFromWindow(ProfileActivity.this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
                            progressDialog =
                                    ProgressDialog.show(ProfileActivity.this, "Salvando",
                                            "Aguarde...", true, false);
                        } else {
                            progressDialog.dismiss();

                        }
                    }
                });
        binding.getViewProfile().changeOK.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                String isChanged = binding.getViewProfile().changeOK.get();
                if (isChanged.equals("true")) {
                    Toast.makeText(ProfileActivity.this, "Perfil Alterado", Toast.LENGTH_SHORT).show();
                    binding.edt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_edit, 0);
                    binding.edt.setOnTouchListener(null);
                } else if (isChanged.equals("false")) {
                    Toast.makeText(ProfileActivity.this, "Falha ao atualizar perfil", Toast.LENGTH_SHORT).show();
                    binding.getViewProfile().avatar.set(getIntent().getStringExtra("avatar"));
                    binding.getViewProfile().name.set(getIntent().getStringExtra("name"));
                }
                binding.getViewProfile().changeOK.set("");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            binding.getViewProfile().avatar.set(binding.getViewProfile().base64FromUri(data.getData(), ProfileActivity.this));
            onClickSave();

        } else if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            binding.getViewProfile().avatar.set(binding.getViewProfile().base64FromExtras(data.getExtras()));
            onClickSave();
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void setStatusBarGradiant(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setNavigationBarColor(Color.parseColor("#2D2553"));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(ProfileActivity.this, "O acesso à câmera necessita de permissão", Toast.LENGTH_LONG).show();
            }
        }
    }
}
