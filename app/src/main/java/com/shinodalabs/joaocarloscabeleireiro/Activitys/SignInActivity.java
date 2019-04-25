package com.shinodalabs.joaocarloscabeleireiro.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.GOOGLE_SIGN_IN_CODE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts.TypefaceBold;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts.TypefaceLight;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView btnFacebook, btnGoogle;
    private TextView tvFacebook, tvGoogle, tvText;
    private Toolbar toolbar;
    private ImageButton btnBack;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        castWidgets();
        setTypeface();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Toasts.toastInfo(getApplicationContext(), getString(R.string.login_canceled));
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnFacebook:

                break;
            case R.id.btnGoogle:
                signInGoogle();
                break;
            case R.id.btnBack:
                finish();
                break;
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mUser = mAuth.getCurrentUser();
                    Toasts.toastSuccess(getApplicationContext(), "Login Success");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasts.toastError(getApplicationContext(), e.getMessage());
            }
        });
    }


    private void signInGoogle() {
        Intent signInGogleIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInGogleIntent, GOOGLE_SIGN_IN_CODE);
    }

    private void setTypeface() {
        tvGoogle.setTypeface(TypefaceLight(getApplicationContext()));
        tvFacebook.setTypeface(TypefaceLight(getApplicationContext()));
        tvText.setTypeface(TypefaceBold(getApplicationContext()));
    }

    private void castWidgets() {
        btnFacebook = findViewById(R.id.btnFacebook);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvFacebook = findViewById(R.id.tvFacebook);
        tvGoogle = findViewById(R.id.tvGoogle);
        tvText = findViewById(R.id.tvText);
        btnBack = findViewById(R.id.btnBack);
        btnFacebook.setOnClickListener(this);
        btnGoogle.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

}
