package hello.world.button.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import hello.world.button.View.LocConActivity;
import hello.world.button.OnSettingListener;
import hello.world.button.R;

import static hello.world.button.Utils.Regex.isArea_code_telephone;
import static hello.world.button.Utils.Regex.isMobile;

public class MyDialog{

	private static MyDialog instance;


    private MyDialog (){}

    public static synchronized MyDialog getInstance() {
        if (instance == null) {
            instance = new MyDialog();
        }
        return instance;
    }

    public static AlertDialog dialog;
    public static void closeDialog(){
        dialog.dismiss();
    }
    public static void showCustomizeDialog(final Context mContext) {
         dialog = new AlertDialog.Builder(mContext)
                .setTitle("填写新联系人信息")
                .setView(R.layout.mydialog)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", null)
                .setNeutralButton("选择联系人", null)
                .setCancelable(true)
                .create();
        dialog.show();
        //为了避免点击 按钮后直接关闭 dialog,把点击事件拿出来设置
        //todo 联系人按钮 增加读取联系人  （选择联系人按钮）
        dialog.getButton(AlertDialog.BUTTON_NEUTRAL)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(mContext, LocConActivity.class);
                        mContext.startActivity(intent);
                    }
                });
        //添加完成确定按钮
        dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText edit_name =
                                (EditText) dialog.findViewById(R.id.name);
                        EditText edit_phone =
                                (EditText) dialog.findViewById(R.id.phone);
                        String phoneNum = edit_phone.getText().toString();
                        String name = edit_name.getText().toString();
                        if ((!name.equals(""))) {
                            //正则表达式判断电话号码
                            if(  Character.isDigit(name.charAt(name.length()-1))   ){
                                Toast.makeText(mContext, "名字最后一位不能为数字", Toast.LENGTH_SHORT).show();
                            }else{
                                if (isArea_code_telephone(phoneNum) || isMobile(phoneNum)) {
                                    //添加联系人
                                    onSettingListener.onSetSucc(edit_name.getText().toString(), phoneNum);
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(mContext, "电话格式不对,11为号码，固话加区号", Toast.LENGTH_SHORT).show();
                                }
                            }

                        } else {
                            Toast.makeText(mContext, "名字不能为空", Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    private static OnSettingListener onSettingListener;

    public void onSetSuccListener(OnSettingListener onSettingListener) {
        this.onSettingListener = onSettingListener;
    }
}
