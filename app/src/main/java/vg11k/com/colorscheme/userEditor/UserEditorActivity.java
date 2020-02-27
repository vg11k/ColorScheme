package vg11k.com.colorscheme.userEditor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import vg11k.com.colorscheme.R;

public class UserEditorActivity extends AppCompatActivity {

    public static final String ACTIVITY_TITLE = "User Editor";
    public static final String ACTIVITY_FEATURE_ID = "User_Editor_Toll";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_editor);
    }
}
