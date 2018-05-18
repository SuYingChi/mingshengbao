package com.msht.minshengbao.FunctionView.fragmeht;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.msht.minshengbao.FunctionView.Public.QRCodeScan;
import com.msht.minshengbao.R;
import com.msht.minshengbao.Utils.MPermissionUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class IcCard extends Fragment {
    private static  final int MY_CAMERA_REQUEST=1;

   private Context mContext;
    public IcCard() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_ic_card, container, false);
        mContext=getActivity();
        view.findViewById(R.id.id_btn_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
            }
        });
        return view;
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                MPermissionUtils.requestPermissionsResult(this, MY_CAMERA_REQUEST, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new MPermissionUtils.OnPermissionListener() {
                    @Override
                    public void onPermissionGranted(int Code) {
                        GoScanActivity();
                    }
                    @Override
                    public void onPermissionDenied(int Code) {
                        Toast.makeText(mContext,"没有权限您将无法进行扫描操作！",Toast.LENGTH_SHORT).show();
                    }
                });

            }else {
                GoScanActivity();
            }
        }else {
            GoScanActivity();
        }
    }
    private void GoScanActivity() {
        Intent intent =new Intent(getActivity(), QRCodeScan.class);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode==MY_CAMERA_REQUEST){
            MPermissionUtils.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }else {

        }
    }

}
