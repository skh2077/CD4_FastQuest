package com.example.tt;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;

import com.example.tt.data.User;

public class Reload extends Activity {

    static SharedPreferences save;
    static SharedPreferences.Editor editor;
    Activity activity;
    public Reload(Activity activity){
        this.activity = activity;
    }

    public void clicked_reload(int reload_num) {
        save = this.activity.getSharedPreferences("mysave", MODE_PRIVATE);
        editor = save.edit();
        if (reload_num > 0) {
            reload_num--;
            editor.putInt("reload", reload_num);
            editor.remove("activity");
            editor.apply();
            this.activity.startActivity(new Intent(this.activity.getApplicationContext(), CardInfo.class));
            this.activity.finish();
        } else {
            new AlertDialog.Builder(this.activity).setTitle("Reload 횟수가 끝났습니다.\n 포기하겠습니까?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    editor.remove("reload");
                    editor.remove("page");
                    editor.remove("activity");
                    editor.apply();
                    //스코어 하락 시킬 것
                    Edit_score editScore = new Edit_score(activity);
                    User user = User.getInstance();
                    editScore.edit_score(user.getUsername(), -5);
                    activity.startActivity(new Intent(activity.getApplicationContext(), MainActivity.class));
                    activity.finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    dialog.cancel();
                }
            }).create().show();
        }
    }
}
