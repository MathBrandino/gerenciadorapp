package br.com.caelum.gerenciadorapp.permissao;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

/**
 * Created by matheus on 18/02/16.
 */
public class Permissao {

    private static final int CODE = 123;
    private static ArrayList<String> list = new ArrayList<>();

    public static void fazPermissao(Activity activity) {

        String[] permissoes = {Manifest.permission.CALL_PHONE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String permissao : permissoes) {

                if (activity.checkSelfPermission(permissao) != PackageManager.PERMISSION_GRANTED) {
                    list.add(permissao);
                }
            }
            request(activity);
        }
    }

    private static void request(Activity activity) {
        String[] array = list.toArray(new String[]{});

        if (list.size() > 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.requestPermissions(array, CODE);
            }
        }
    }

    public static void verifica(String[] permissions, int[] grantResults, Activity activity) {

        list.clear();
        for (int i = 0; i < permissions.length; i++) {

            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                list.add(permissions[i]);
            }
        }

        request(activity);
    }


}
