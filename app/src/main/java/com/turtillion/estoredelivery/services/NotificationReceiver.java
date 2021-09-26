package com.turtillion.estoredelivery.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.app.RemoteInput;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.turtillion.estoredelivery.volleyservice.VolleyInterface;

import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class NotificationReceiver extends BroadcastReceiver implements VolleyInterface {

    String userid = "";
    String oppoid = "";
    int notiid = -1;
    SharedPreferences shared;
    Context mContext;
    CharSequence name;

    String TAG = "NotificationReceiver";


    DatabaseReference mDatabase;

    ArrayList<String> useridArray;

    String title = "";
    String msg = "";


    @Override
    public void onReceive(Context context, Intent intent) {
        //getting the remote input bundle from intent

        shared = context.getSharedPreferences("my_pref", MODE_PRIVATE);

        mContext = context;

        mDatabase = FirebaseDatabase.getInstance().getReference();

        useridArray = new ArrayList<>();

        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);

//        userid = intent.getStringExtra("userId");
//        oppoid = intent.getStringExtra("user");
//        title = intent.getStringExtra("title");
//        msg = intent.getStringExtra("msg");
//        notiid = intent.getIntExtra("notiid", -1);
//
//
//        if (remoteInput != null) {
//
//            name = remoteInput.getCharSequence("key_reply_message");
//
//
//            if (notiid != -1) {
//                showReplyNotification(title, String.valueOf(name), userid, oppoid);
//
//            }
//
//            if (oppoid != null) {
//                keyfuntion(oppoid);
//            }
//
//        }

        //if more button is clicked
//        if (intent.getIntExtra(KEY_INTENT_DISMISS, -1) == REQUEST_CODE_DISMISS) {
//
//            final NotificationManagerCompat mNotificationManager = NotificationManagerCompat.from(context.getApplicationContext());
//            if (notiid != -1) {
//                mNotificationManager.cancel(notiid);
//            }
//        }
    }


    private void showReplyNotification(String title, String message, String userId, String user) {

        Intent mIntent = null;

//        mIntent = new Intent(mContext, MainNavigation.class);
//        mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        mIntent.putExtra("type", "message");
//        mIntent.putExtra("userId", userId);
//
//
//        int randomRequestCode = new Random().nextInt(54325);
//
//        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0 /* Request code */, mIntent,
//                PendingIntent.FLAG_ONE_SHOT);
//
//        Intent resultIntent = new Intent(mContext, NotificationReceiver.class);
//        resultIntent.putExtra("userId", userId);
//        resultIntent.putExtra("user", user);
//        resultIntent.putExtra("notiid", notiid);
//        resultIntent.putExtra("msg", message);
//        resultIntent.putExtra("title", title);
//        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//
//
//        //Set a unique request code for this pending intent
//        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(mContext, randomRequestCode,
//                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//
////        PendingIntent dismissIntent = PendingIntent.getBroadcast(this, REQUEST_CODE_DISMISS,
////                resultIntent.putExtra(KEY_INTENT_DISMISS, REQUEST_CODE_DISMISS), PendingIntent.FLAG_UPDATE_CURRENT);
//
//
//        String replyLabel = mContext.getResources().getString(R.string.notif_action_reply);
//        RemoteInput remoteInput = new RemoteInput.Builder("key_reply_message")
//                .setLabel(replyLabel)
//                .build();
//
//
//        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
//                R.drawable.send_black_icon, replyLabel, resultPendingIntent)
//                .addRemoteInput(remoteInput)
//                .setAllowGeneratedReplies(true)
//
//                .build();
//
//
//
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(mContext, CHANNEL_ONE_ID)
//                        .setSmallIcon(R.drawable.pista_icon)
//                        .setContentTitle(title)
//                        .setContentText(message)
//                        .setGroup("message")
//                        .addAction(replyAction)
////                        .addAction(0, "Dismiss", dismissIntent)
//                        .setSound(defaultSoundUri)
//
//                        .setColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
//                        .setGroupSummary(true)
//                        .setAutoCancel(true)
//                        .setContentIntent(pendingIntent);
//
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_LOW);
//        } else {
//            notificationBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
//        }
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // Since android Oreo notification channel is needed.
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID,
//                    CHANNEL_ONE_NAME,
//                    NotificationManager.IMPORTANCE_HIGH);
//            if (notificationManager != null) {
//                notificationManager.createNotificationChannel(channel);
//                notificationManager.notify(notiid/* ID of notification */, notificationBuilder.build());
//            }
//        }


    }


    private void sendfuntion(String pushkey) {
//
//        if (name != "") {
//
////new message list
//            final Map<String, Object> sendmsgmap = new HashMap<>();
//            sendmsgmap.put("content", name);
//            sendmsgmap.put("created_at", ServerValue.TIMESTAMP);
//            sendmsgmap.put("sender", userid);
//            sendmsgmap.put("type", "text");
//
////reciever chat list
//            final Map<String, Object> oppolistmap = new HashMap<>();
//            oppolistmap.put("chat_id", pushkey);
//            oppolistmap.put("last_updated_at", ServerValue.TIMESTAMP);
//            oppolistmap.put("unread_count", 1);
//            oppolistmap.put("l", name);
//            oppolistmap.put("t", "text");
//            oppolistmap.put("t2", "text");
//            oppolistmap.put("s", userid);
//
////sender chat list
//            Map<String, Object> senderChatList = new HashMap<>();
//            senderChatList.put("last_updated_at", ServerValue.TIMESTAMP);
//            senderChatList.put("l", name);
//            senderChatList.put("t", "text");
//            senderChatList.put("t2", "text");
//            senderChatList.put("s", userid);
//
//
//// update reciever side unread count / reciver chat list
//
//            String reciverPath = "users/" + oppoid + "/chatLists/" + userid;
//
//
//            mDatabase.child(reciverPath).runTransaction(new Transaction.Handler() {
//                @NonNull
//                @Override
//                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//
//
//                    Map<String, Object> data = (Map<String, Object>) mutableData.getValue();
//
//                    if (data == null) {
//
//                        Common.showlog("testchat", "chatlist equal null");
//                        mutableData.setValue(oppolistmap);
//                        return Transaction.success(mutableData);
//                    } else {
//
//                        long unreadCount = (long) data.get("unread_count");
//
//                        unreadCount += 1;
//
//                        oppolistmap.put("unread_count", unreadCount);
//
//                        mutableData.setValue(oppolistmap);
//                    }
//
//                    return Transaction.success(mutableData);
//                }
//
//                @Override
//                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                    // Transaction completed
//                    if (databaseError != null) {
//                        Common.showlog("Firebase", "postTransaction:onComplete:" + databaseError);
//                    }
//
//                }
//            });
//
//
//// reciever chat side putting message
//
//            String reciverchatpath = "users/" + oppoid + "/chats/" + pushkey + "/messages";
//
//            Map<String, Object> reciverChatVaue = new HashMap<>();
//
//            String reciverkey = mDatabase.child(reciverchatpath).push().getKey();
//
//            reciverChatVaue.put(reciverkey, sendmsgmap);
//
//            mDatabase.child(reciverchatpath).updateChildren(reciverChatVaue);
//
//
////reciver total count
//
//            String reciverCountPath = "users/" + oppoid + "/chats/" + pushkey + "/metadata/total";
//
//
//            mDatabase.child(reciverCountPath).runTransaction(new Transaction.Handler() {
//                @NonNull
//                @Override
//                public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//
//                    //    Long data = Long.valueOf((long)mutableData.getValue());
//                    Common.showlog("mutable", String.valueOf(mutableData.getValue()));
//
//                    if (mutableData.getValue() == null) {
//
//                        mutableData.setValue(1);
//                        return Transaction.success(mutableData);
//                    }
//
//                    long total = (long) mutableData.getValue();
//
//                    total += 1;
//                    mutableData.setValue(total);
//
//                    return Transaction.success(mutableData);
//                }
//
//                @Override
//                public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                    // Transaction completed
//                    Common.showlog("Firebase", "postTransaction:onComplete:" + databaseError);
//                }
//            });
//
//// sender chat list update
//
//            String path = "users/" + userid + "/chatLists/" + oppoid;
//            mDatabase.child(path).updateChildren(senderChatList);
//
//
//// sender chat update
//
//            String chatPath = "users/" + userid + "/chats/" + pushkey + "/messages";
//
//            Map<String, Object> senderChatVaue = new HashMap<>();
//
//            String key = mDatabase.child(chatPath).push().getKey();
//            senderChatVaue.put(key, sendmsgmap);
//
//            mDatabase.child(chatPath).updateChildren(senderChatVaue);
//
//
//        }
//
//        //sender total count
//
//        String senderCountPath = "users/" + userid + "/chats/" + pushkey + "/metadata/total";
//
//
//        mDatabase.child(senderCountPath).runTransaction(new Transaction.Handler() {
//            @NonNull
//            @Override
//            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
//
//
//                if (mutableData.getValue() == null) {
//
//                    mutableData.setValue(1);
//                    return Transaction.success(mutableData);
//                }
//
//                long total = (long) mutableData.getValue();
//
//                total += 1;
//                mutableData.setValue(total);
//
//                return Transaction.success(mutableData);
//            }
//
//            @Override
//            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
//                // Transaction completed
//                Common.showlog("Firebase", "postTransaction:onComplete:" + databaseError);
//            }
//        });
//
//        notifyReceiver("text");


    }

    private void notifyReceiver(String type) {

//        useridArray.clear();
//        useridArray.add(oppoid);
//
//        String url = UrlConstant.SEND_MESSAGE_NOTIFY;
//        HashMap<String, Object> params = new HashMap<>();
//        params.put("to", useridArray);
//        params.put("message", name);
//        params.put("type", type);
//        VolleyUtils volleyUtils = new VolleyUtils();
//        volleyUtils.callnetworkrequest(mContext, this, 1, url, params, TAG, UrlConstant.REQ_SEND_MESSAGE_NOTIFY, 1, false, false);

    }

    @Override
    public void SuccessResponse(JSONObject response, int requestcode) {

    }

    @Override
    public void ErrorResponse(String msg, int requestcode) {

    }

    private void keyfuntion(final String oppoid) {

//        SharedPreferences shared = Objects.requireNonNull(mContext).getSharedPreferences("my_pref", MODE_PRIVATE);
//        final String userid = shared.getString("userid", "");
//
//        final DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("chatLists").child(oppoid);
//        final DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("chats");
//        final DatabaseReference ref3 = FirebaseDatabase.getInstance().getReference().child("users").child(oppoid).child("chatLists").child(userid);
//
//        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                if (dataSnapshot.getValue() != null) {
//
//                    if (dataSnapshot.child("chat_id").getValue() != null && dataSnapshot.child("last_updated_at").getValue() != null && dataSnapshot.child("unread_count").getValue() != null) {
//                        String pushkey = Objects.requireNonNull(dataSnapshot.child("chat_id").getValue()).toString();
//                        Common.showlog("chatter", "chatter: " + pushkey);
//
//                        sendfuntion(pushkey);
//
//                    }
//
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });

    }
}
