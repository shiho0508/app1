package com.example.app1.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.app1.MyProfileFragment;
import com.example.app1.R;

public class SettingsAccountFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_account, container, false);
        //Button change = view.findViewById(R.id.button_settings_account_change_password);
        //Button change = view.findViewById(R.id.button_settings_account_change_mail_address);
        Button ChangeProfile = view.findViewById(R.id.button3);

        ChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyProfileFragment()).commit();
            }
        });
        return view;
    }
}
