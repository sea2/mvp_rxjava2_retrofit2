package com.lhy.mvp_rxjava_retrofit.util;

import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import com.lhy.mvp_rxjava_retrofit.http.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lhy on 2017/4/12.
 */

public class PermissionsUtil {

    public static String[] getNeedApplyPermissions(String[] strArray) {
        if (strArray != null && strArray.length > 0) {
            List<String> list = new ArrayList<>();
            for (String str : strArray) {
                if (ActivityCompat.checkSelfPermission(Utils.getContext(), str) != PackageManager.PERMISSION_GRANTED) {
                    list.add(str);
                }
            }
            if (list.size() > 0) return list.toArray(new String[list.size()]);
            else return null;
        } else return null;
    }


}
