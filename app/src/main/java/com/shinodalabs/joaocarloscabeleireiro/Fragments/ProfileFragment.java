package com.shinodalabs.joaocarloscabeleireiro.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Fonts;
import com.shinodalabs.joaocarloscabeleireiro.Utils.Toasts;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ID_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.PHONE_USER;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.RESULT;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.URL_200;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.URL_400;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.URL_404;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_EDIT_USER_PROFILE;
import static com.shinodalabs.joaocarloscabeleireiro.Utils.Url.URL_GET_USER_DATA;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {

    private View v;
    private TextView tvPersonalData;
    private TextInputLayout tilName, tilEmail, tilWhats;
    private TextInputEditText etName, etEmail, etWhats;
    private Button btnSave;
    private KProgressHUD dialog;
    private FirebaseAuth mAuth;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();

        castWidgets();
        setTypeface();
        setText();

        etWhats.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s != "") {
                    tilWhats.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return v;
    }

    private void setText() {
        createDialog(getString(R.string.please_wait), getString(R.string.getting_data));
        Ion.with(getContext())
                .load(URL_GET_USER_DATA)
                .setBodyParameter(ID_USER, mAuth.getCurrentUser().getUid())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String r = result.get(RESULT).getAsString();

                            if (r.equals(URL_200)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                etEmail.setText(mAuth.getCurrentUser().getEmail());
                                etName.setText(mAuth.getCurrentUser().getDisplayName());
                                etWhats.setText(result.get(PHONE_USER).getAsString());
                            } else if (r.equals(URL_400)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }
                        } catch (Exception x) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toasts.toastError(getContext(), x.getMessage());
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                final String number = etWhats.getText().toString();
                if (number.equals("")) {
                    tilWhats.setError(getString(R.string.fill_whats_app));
                } else {
                    new MaterialStyledDialog.Builder(getContext())
                            .setTitle(getString(R.string.edit_profile))
                            .setIcon(R.drawable.save)
                            .setDescription(String.format("%s\n\n %s\n\n%s", getString(R.string.check_whats_app), number, getString(R.string.wish_save)))
                            .setPositiveText(getString(R.string.confirm))
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    createDialog(getString(R.string.please_wait), getString(R.string.saving_data));
                                    updateProfile(number);
                                }
                            })
                            .setNegativeText(getString(R.string.cancel))
                            .onNegative(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
                break;
        }
    }

    private void updateProfile(String number) {
        Ion.with(getContext())
                .load(URL_EDIT_USER_PROFILE)
                .setBodyParameter(PHONE_USER, number)
                .setBodyParameter(ID_USER, mAuth.getCurrentUser().getUid())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String r = result.get(RESULT).getAsString();

                            if (r.equals(URL_200)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toasts.toastSuccess(getContext(), getString(R.string.success_save_data));
                            } else if (r.equals(URL_404)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toasts.toastError(getContext(), getString(R.string.erro_save_user_data));
                            }
                        } catch (Exception x) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toasts.toastError(getContext(), x.getMessage());
                        }

                    }
                });
    }

    private void setTypeface() {
        tvPersonalData.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tilName.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tilEmail.setTypeface(Fonts.TypefaceLight(v.getContext()));
        tilWhats.setTypeface(Fonts.TypefaceLight(v.getContext()));
        etWhats.setTypeface(Fonts.TypefaceLight(v.getContext()));
        etName.setTypeface(Fonts.TypefaceLight(v.getContext()));
        etEmail.setTypeface(Fonts.TypefaceLight(v.getContext()));
        btnSave.setTypeface(Fonts.TypefaceLight(v.getContext()));
    }

    private void castWidgets() {
        tvPersonalData = v.findViewById(R.id.tvPersonalData);
        tilName = v.findViewById(R.id.tilDisplayName);
        tilEmail = v.findViewById(R.id.tilEmail);
        tilWhats = v.findViewById(R.id.tilWhatsApp);
        etEmail = v.findViewById(R.id.etEmail);
        etName = v.findViewById(R.id.etDisplayName);
        etWhats = v.findViewById(R.id.etWhatsApp);
        btnSave = v.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);
    }

    private void createDialog(String title, String message) {
        dialog = KProgressHUD.create(getContext())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(title)
                .setDetailsLabel(message)
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);
        dialog.show();
    }

}
