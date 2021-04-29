package co.kaua.palacepetz.Methods;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class Userpermissions {

    public static void validatePermissions(String[] permissoes, Activity activity, int requestCode){
        if (Build.VERSION.SDK_INT >= 23){
            ArrayList<String> PermissionList = new ArrayList<>();

            //  Checks permissions already granted
            for (String permission : permissoes) {
                boolean HavePermission = ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
                if (!HavePermission){
                    PermissionList.add(permission);
                }
            }
            if (!PermissionList.isEmpty()){
                String[] PermissionsVector = new String[PermissionList.size()];
                PermissionList.toArray(PermissionsVector);
                ActivityCompat.requestPermissions(activity, PermissionsVector, requestCode);
            }
        }
    }
}
