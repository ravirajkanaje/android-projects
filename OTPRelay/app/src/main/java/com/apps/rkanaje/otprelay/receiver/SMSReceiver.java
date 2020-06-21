package com.apps.rkanaje.otprelay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.apps.rkanaje.otprelay.gmail.GmailSender;
import com.apps.rkanaje.otprelay.service.RelayUser;
import com.apps.rkanaje.otprelay.service.RelayUserService;

import java.util.List;

public class SMSReceiver extends BroadcastReceiver {

    public static final String ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public static final String TEXT_OTP = "otp";

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "received sms?", Toast.LENGTH_LONG).show();
        if (intent.getAction().equals(ANDROID_PROVIDER_TELEPHONY_SMS_RECEIVED)) {
//            Toast.makeText(context, "message received", Toast.LENGTH_SHORT).show();
            final Bundle bundle = intent.getExtras();
            try {
                if (bundle != null) {
                    final Object[] pdus = (Object[]) bundle.get("pdus");
                    for (int i = 0; i < pdus.length; i++) {
                        SmsMessage smsMessage;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i], bundle.getString("format"));
                        } else {
                            smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        }
                        final String from = smsMessage.getDisplayOriginatingAddress();
                        final String msgBody = smsMessage.getMessageBody();

                        if (msgBody.toLowerCase().contains(TEXT_OTP)) {
                            final RelayUserService relayUserService = new RelayUserService(context);
                            List<RelayUser> allUsers = relayUserService.getAllUsers();
                            if (null != allUsers) {
                                String email = allUsers.get(0).getEmail();
                                relaySMS(from, msgBody, email);
                            }
                        }
                        /*
                        final RelayUser relayUser = relayUserService.getByPhone(from);
                        if (null != relayUser) {
                            final String msgBody = smsMessage.getMessageBody();
                            handleMessage(context, from, msgBody);
                        } else {
                            // ignored safely
                        }
                        */
                    }
                }
            } catch (Exception e) {
                Log.d("Exception caught", e.getMessage());
            }
        } else {
            //Toast.makeText(context, "UNKNOWN: " + intent.getAction(), Toast.LENGTH_LONG).show();
        }
    }

    private void relaySMS(String from, String msgBody, String to) {
        String text = String.format("from: %s, body: %s", from, msgBody);
        //Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        final GmailSender sender = new GmailSender(to, "SMS relayed from: " + from, text);
        sender.execute();
    }
}
