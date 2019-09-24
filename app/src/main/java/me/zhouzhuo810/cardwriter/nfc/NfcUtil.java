package me.zhouzhuo810.cardwriter.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.util.Log;


public class NfcUtil {
    private static String TAG = NfcUtil.class.getSimpleName();

    //初次判断是什么类型的NFC卡
    public static NdefMessage[] getNdefMsg(Intent intent) {
        if (intent == null)
            return null;
        //nfc卡支持的格式
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String[] temp = tag.getTechList();
        for (String s : temp) {
            Log.i(TAG, "resolveIntent tag: " + s);
        }
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            Parcelable[] rawMessage = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] ndefMessages = new NdefMessage[0];

            // 判断是哪种类型的数据 默认为NDEF格式
            if (rawMessage != null) {
                Log.i(TAG, "getNdefMsg: ndef格式 ");
                ndefMessages = new NdefMessage[rawMessage.length];
                for (int i = 0; i < rawMessage.length; i++) {
                    ndefMessages[i] = (NdefMessage) rawMessage[i];
                }
            } else {
                //未知类型 (公交卡类型)
                Log.i(TAG, "getNdefMsg: 未知类型");
                //对应的解析操作，在Github上有
            }
            return ndefMessages;
        }
        return null;
    }

    public static String getTestRfId(Intent intent) {
        if (intent == null)
            return null;
        //nfc卡支持的格式
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) ||
                NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
            return StringUtil.bytesToHexString(tag.getId());
        }
        return null;
    }

}
