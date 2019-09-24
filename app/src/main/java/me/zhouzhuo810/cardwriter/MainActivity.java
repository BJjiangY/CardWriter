package me.zhouzhuo810.cardwriter;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import me.zhouzhuo810.cardwriter.nfc.NfcUtil;
import me.zhouzhuo810.magpie.utils.SpUtil;
import me.zhouzhuo810.magpie.utils.ToastUtil;

public class MainActivity extends NFCBaseActivity {
    
    private EditText mEtContent;
    
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }
    
    @Override
    public boolean shouldSupportMultiLanguage() {
        return false;
    }
    
    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        mEtContent = findViewById(R.id.et_number);
    }
    
    @Override
    public void initData() {
        mEtContent.setText(SpUtil.getString("cardNumber"));
    }
    
    @Override
    public void initEvent() {
        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.e("TTT", "s=" + s);
            }
            
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            
            }
            
            @Override
            public void afterTextChanged(Editable s) {
                SpUtil.putString("cardNumber", s.toString().trim());
            }
        });
    }
/*
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //写卡
        Tag detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeNFCTag(detectedTag);
        
    }
    */
    @Override
    public void onNewIntent(Intent intent) {
        //读卡
        try {
            String msg = NfcUtil.getTestRfId(intent);
            if (msg == null) {
                ToastUtil.showToast("不支持识别该类型的卡");
                return;
            }
            mEtContent.setText(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 往标签写数据的方法
     */
    public void writeNFCTag(Tag tag) {
        if (tag == null) {
            return;
        }
        String content = SpUtil.getString("cardNumber");
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showToast("请输入卡号");
            return;
        }
        //        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{NdefRecord.createApplicationRecord(mPackageName)});
        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{
            NdefRecord.createTextRecord("en", content)
        });
        //转换成字节获得大小
        int size = ndefMessage.toByteArray().length;
        try {
            //2.判断NFC标签的数据类型（通过Ndef.get方法）
            Ndef ndef = Ndef.get(tag);
            //判断是否为NDEF标签
            if (ndef != null) {
                ndef.connect();
                //判断是否支持可写
                if (!ndef.isWritable()) {
                    return;
                }
                //判断标签的容量是否够用
                if (ndef.getMaxSize() < size) {
                    return;
                }
                //3.写入数据
                ndef.writeNdefMessage(ndefMessage);
                Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
            } else { //当我们买回来的NFC标签是没有格式化的，或者没有分区的执行此步
                //Ndef格式类
                NdefFormatable format = NdefFormatable.get(tag);
                //判断是否获得了NdefFormatable对象，有一些标签是只读的或者不允许格式化的
                if (format != null) {
                    //连接
                    format.connect();
                    //格式化并将信息写入标签
                    format.format(ndefMessage);
                    Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
}
