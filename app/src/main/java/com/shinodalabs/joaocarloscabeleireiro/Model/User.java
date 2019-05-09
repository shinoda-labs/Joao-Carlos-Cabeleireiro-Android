package com.shinodalabs.joaocarloscabeleireiro.Model;

import com.google.firebase.auth.FirebaseAuth;

import static com.shinodalabs.joaocarloscabeleireiro.Utils.Const.ADM;

public class User {

    public boolean isAdm(FirebaseAuth mAuth) {
        if (mAuth.getCurrentUser().getUid().equals(ADM)) {
            return true;
        } else {
            return false;
        }
    }

}
