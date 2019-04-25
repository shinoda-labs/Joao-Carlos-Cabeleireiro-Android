package com.shinodalabs.joaocarloscabeleireiro.Utils;

import android.content.Context;
import android.widget.Toast;
import es.dmoral.toasty.Toasty;

public class Toasts {

    public static void toastSuccess(Context context, String msg) {
        Toasty.success(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void toastError(Context context, String msg) {
        Toasty.error(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void toastInfo(Context context, String msg) {
        Toasty.info(context, msg, Toast.LENGTH_LONG, true).show();
    }

    public static void toastWarning(Context context, String msg) {
        Toasty.warning(context, msg, Toast.LENGTH_LONG, true).show();
    }

}
