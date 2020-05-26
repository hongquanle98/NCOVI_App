package com.example.ncovi_app.UI.Home;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ncovi_app.R;
import com.example.ncovi_app.Model.UserInfo;
import com.example.ncovi_app.DB.UserInfoDB;
import com.example.ncovi_app.UI.SucKhoe.SucKhoeFragment;
import com.example.ncovi_app.databinding.DialogReminderBinding;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;

public class ReminderDialog extends DialogFragment {
    DialogReminderBinding binding;
    UserInfoDB userInfoDB;
    //Được dùng khi khởi tạo dialog mục đích nhận giá trị
//    public static ReminderDialog newInstance(boolean flag) {
//        ReminderDialog dialog = new ReminderDialog();
//        Bundle args = new Bundle();
//        args.putBoolean("flag", flag);
//        dialog.setArguments(args);
//        return dialog;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_reminder, container, false);
        userInfoDB = new UserInfoDB(getActivity());
        getUserName();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.container, new SucKhoeFragment()).commit();
                getDialog().dismiss();
            }
        });
        binding.btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });
    }
    public void getUserName() {
        if (userInfoDB.getCount() > 0) {
            Cursor cursor = userInfoDB.getAllData();
            UserInfo userInfo= new UserInfo();
            while (cursor.moveToNext()) {
                userInfo = new UserInfo();

                userInfo.setFullName(cursor.getString(0));
            }
            binding.txtHello.setText("Xin chào "+ userInfo.getFullName()+"!");
        }
    }
}
