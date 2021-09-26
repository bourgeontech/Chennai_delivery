//package com.Turtillion.EstoreDeliveryBoy.utils;
//
//import android.annotation.SuppressLint;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.coordinatorlayout.widget.CoordinatorLayout;
//
//import com.google.android.material.bottomsheet.BottomSheetBehavior;
//import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
//import com.google.firebase.database.annotations.NotNull;
//import com.Turtillion.EstoreDeliveryBoy.R;
//
//public class RegisterSuccessFragment extends BottomSheetDialogFragment {
//
//    String msg;
//
//    public RegisterSuccessFragment(String msg) {
//        this.msg = msg;
//        // Required empty public constructor
//    }
//
//
//    OptionListener optionListener;
//
//    public void setOptionListener(com.Turtillion.EstoreDeliveryBoy.utils.RegisterSuccessFragment.OptionListener optionListener) {
//        this.optionListener = optionListener;
//    }
//
//    public interface OptionListener {
//        void optionSelected(String item);
//    }
//
//    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {
//
//        @Override
//        public void onStateChanged(@NonNull View bottomSheet, int newState) {
//            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
//                dismiss();
//            }
//
//        }
//
//        @Override
//        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//        }
//    };
//
//
//    @SuppressLint("RestrictedApi")
//    @Override
//    public void setupDialog(@NotNull final Dialog dialog, int style) {
//        super.setupDialog(dialog, style);
//
//        View contentView = View.inflate(getContext(), R.layout.fragment_register_success, null);
//        dialog.setContentView(contentView);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
//        CoordinatorLayout.Behavior behavior = params.getBehavior();
//
//        if (behavior instanceof BottomSheetBehavior) {
//            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
//        }
//        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));
//
//
//        TextView submit = contentView.findViewById(R.id.submit);
//        TextView welcomeMsg = contentView.findViewById(R.id.welcomeMsg);
//        welcomeMsg.setText(msg);
//
//        submit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getActivity() != null) {
//                    dialog.dismiss();
//                    optionListener.optionSelected("");
//                }
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//    }
//
//    @Override
//    public void onCancel(@NonNull DialogInterface dialog) {
//        super.onCancel(dialog);
//
//    }
//
//
//}
