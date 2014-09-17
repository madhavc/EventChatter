package com.example.moxtraapp;


import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.moxtra.sdk.MXAccountManager;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException.AccountManagerIsNotValid;
import com.moxtra.sdk.MXSDKConfig.MXUserIdentityType;
import com.moxtra.sdk.MXSDKConfig;
import com.moxtra.sdk.MXSDKException;

public class MainActivity extends Activity {
	private static final int DIALOG_SETUP_LOGIN_MODEL = 0;
	private static final int DIALOG_USER_PROFILE = 1;

    public static final String appClientId = "sO07gD9IGVw";
    public static final String appClientSecret ="pkoV1d7pk88";
    public static final String authCallbackUrl = null;
    
    private static final String TAG = "MainActivity";

    private MXAccountManager accountMgr;
    


    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
            accountMgr = MXAccountManager.createInstance(getApplicationContext(), appClientId, appClientSecret);
        } catch (MXSDKException.InvalidParameter e) {
            e.printStackTrace();
        }
		MXChatManager.getInstance().setMoreButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick(), callback from Moxtra!!");
            }
        });
		
		accountMgr.setAppInfo(appClientId, appClientSecret, authCallbackUrl);
		final MXSDKConfig.MXUserInfo user = new MXSDKConfig.MXUserInfo("Madhav", MXUserIdentityType.IdentityUniqueId);
		MXSDKConfig.MXProfileInfo profile = new MXSDKConfig.MXProfileInfo("Madhav", "Chhura", null);
		accountMgr.setupUser(user, profile, null, new MXAccountManager.MXAccountLinkListener() {
		    @Override
		    public void onLinkAccountDone(boolean bSuccess) {
		    	if (bSuccess) {
		    		//TODO
		    	} else {
		    		///Toast.makeText(getApplicationContext(), getString(R.string.login_failed), Toast.LENGTH_LONG).show();
		    	}
		    }
		});
		
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void createChatButtonPressed(View v) {
		try {
			MXChatManager.getInstance().createChat(new MXChatManager.OnCreateChatListener() {
                @Override
                public void onCreateChatSuccess(String binderID) {
                    Log.d(TAG, "onCreateChatSuccess(), binderId = " + binderID);
                    MXChatManager.OnInviteListener callback = new MXChatManager.OnInviteListener() {
                        @Override
                        public void onInviteSuccess() {
                            Toast.makeText(MainActivity.this, "Invite Successfully", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onInviteFailed(int errorCode, String message) {
                            Toast.makeText(MainActivity.this, "Invite Failed. Error: " + message, Toast.LENGTH_LONG).show();
                        }
                    };
        
                }

                @Override
                public void onCreateChatFailed(int errorCode, String message) {
                    Log.d(TAG, "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
                }
            });
		} catch (AccountManagerIsNotValid e) {
			e.printStackTrace();
		}
		 try {
	            MXChatManager.getInstance().openChat("#your_binder_id#", new MXChatManager.OnOpenChatListener() {

	                @Override
	                public void onOpenChatSuccess() {

	                }

	                @Override
	                public void onOpenChatFailed(int errorCode, String message) {

	                }
	            });
	        } catch (AccountManagerIsNotValid e) {
	            e.printStackTrace();
	        }
	}
	
	public void unlinkButtonPressed(View v) {
		if (accountMgr != null && accountMgr.isLinked()) {
            accountMgr.unlinkAccount(new MXAccountManager.MXAccountUnlinkListener() {
                @Override
                public void onUnlinkAccountDone(MXSDKConfig.MXUserInfo user) {
                    Toast.makeText(getApplicationContext(), getString(R.string.unlink_success), Toast.LENGTH_LONG).show();
                    MXChatManager.getInstance().unlink();
                }
            });
        }
		
	}
	
	
	
}
