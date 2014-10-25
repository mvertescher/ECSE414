package com.ecse414.meshsim;

import com.ecse414.meshsim.main.Constants;
import com.ecse414.meshsim.main.MeshNode;
import com.ecse414.meshsim.main.Reciever;
import com.ecse414.meshsim.main.Sender;
import com.ecse414.meshsim.packet.DataPacket;
import com.ecse414.meshsim.setup.LocalConfig;
import com.example.meshsim.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private MeshNode root; 
	private Sender sender;
	private TextView text;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        text = (TextView) findViewById(R.id.text2);
    
        String androidId = Secure.getString(this.getContentResolver(), Secure.ANDROID_ID);
        text.setText(text.getText() + "ANDROID_ID = " + androidId + "\n");
        root = LocalConfig.getRoot(androidId);
        
        if (root == null) {
        	text.setText(text.getText() + "This AVD is not reconized by the conifguration. \n");
        	return;
        } 
        	
        this.setTitle(this.getTitle() + " (" + root.getName() + ")");
        
        // Start the sender 
        sender = new Sender(root);
        new Thread(sender).start();
        
        // Start the receiver 
        new Thread(new Reciever(root, new Handler(), text)).start();
    }

    public void onClick(View view) {
		EditText et = (EditText) findViewById(R.id.EditText01);
		String str = et.getText().toString();
		text.setText(text.getText().toString() + " Me : " + str + "\n");
		System.out.println("Sending message = " + str);
	
		for (MeshNode mn : root.getNeighbors()) {
			String srcAddr = Constants.LOCALHOST;
			String destAddr = Constants.LOCALHOST + ":" + mn.getPort(); 
			this.sender.queuePacket(new DataPacket(srcAddr,destAddr, root.getName() + " : "+ str));
		}
		// SEND MESSAGE 
		//new Thread(new TcpSender(Constants.LOCALHOST,DEST_PORT,str)).start();
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
}
