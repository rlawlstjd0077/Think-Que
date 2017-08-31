package com.example.jsw.thinkque;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by JSW on 2017-08-30.
 */

public class EditNodeFragment extends DialogFragment{
    private EditText contentEditText;
    private Button submitButton;
    private Button cancelButton;


    private SubmitButtonClickListener submitButtonClickListener;

    public interface SubmitButtonClickListener {
        void onSubmitButtonClick(String content);

    }

    public EditNodeFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_node, container);
        submitButton = (Button) view.findViewById(R.id.btn_submit);
        cancelButton = (Button) view.findViewById(R.id.btn_cancel);
        contentEditText = (EditText) view.findViewById(R.id.content_edit);
        contentEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getDialog().setTitle("Enter content");
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitButtonClickListener.onSubmitButtonClick(contentEditText.getText().toString());
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }

    public void setOnSubmitButtonClickListener(SubmitButtonClickListener listener){
        this.submitButtonClickListener = listener;
    }
}
