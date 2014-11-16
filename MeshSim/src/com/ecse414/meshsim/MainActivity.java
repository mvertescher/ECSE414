package com.ecse414.meshsim;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.ecse414.meshsim.main.Constants;
import com.ecse414.meshsim.main.MeshNode;
import com.ecse414.meshsim.main.Reciever;
import com.ecse414.meshsim.main.Sender;
import com.ecse414.meshsim.packet.DataPacket;
import com.ecse414.meshsim.setup.LocalConfig;
import com.example.meshsim.R;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings.Secure;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
		
		//Always listen on localport 5001
		// TODO: this should be a config
		root = new MeshNode(androidId, androidId, Constants.LOCALHOST + ":5001");
		
		searchForNeighbors(root);
		
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
	


	public void searchForNeighbors(final MeshNode root) {
	    final Handler handler = new Handler();
	    Timer timer = new Timer();
	    TimerTask doAsynchronousTask = new TimerTask() {       
	        @Override
	        public void run() {
	            handler.post(new Runnable() {
	                public void run() {       
	                    try {
	                    	HttpAsyncTask performBackgroundTask = new HttpAsyncTask();
	                        performBackgroundTask.execute(root);
	                    } catch (Exception e) {
	                        // TODO Auto-generated catch block
	                    }
	                }
	            });
	        }
	    };
	    timer.schedule(doAsynchronousTask, 0, 10000); //execute in every 10s, this should be a config.
	}


	
	 private class HttpAsyncTask extends AsyncTask<MeshNode, String, MeshNode> {

	        // onPostExecute displays the results of the AsyncTask.
	        @Override
	        protected void onPostExecute(MeshNode result) {
				text.setText(text.getText() + "done Running async task\n");
				for(MeshNode n : result.getNeighbors().values()){
					text.setText(text.getText() + "found neighbor: "+ n.getId() + "\n");
					root.getNeighbors().put(n.getId(), n);
				}
	        }
	        @Override
	        protected void onProgressUpdate(String... progress) {
				text.setText(text.getText() + progress[0] + "\n");
	        }

			@Override
			protected MeshNode doInBackground(MeshNode... arg0) {
				try{
				String androidId = arg0[0].getId();

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost postRequest = new HttpPost("http://10.0.2.2:8080/");
				HttpResponse response = null;
				postRequest.setEntity(new StringEntity(androidId + "\n"));
//				publishProgress("sending request");
				response = httpclient.execute(postRequest);
//				publishProgress("request sent");
				StatusLine statusLine = response.getStatusLine();
				if(statusLine.getStatusCode() == HttpStatus.SC_OK){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();

					String responseString = out.toString();
//					publishProgress("got response " + responseString);

					String[] responseStringTokens = responseString.split("\n");
					MeshNode mesh = new MeshNode(arg0[0].getId(), arg0[0].getId(), arg0[0].getIp() + ":" + arg0[0].getPort());
					for(String s : responseStringTokens){
						if(s == "" || s==" ") continue;
						String[] keypair = s.split(",");
						MeshNode n = new MeshNode(keypair[0], keypair[0], keypair[1]);
						mesh.getNeighbors().put(n.getId(), n);
					}
					return mesh;
				} else{
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
				}catch(Exception e){
					publishProgress("Exception! " + e.getMessage());
				}
				return null;
			}
	    }

	public void onClick(View view) {
		EditText et = (EditText) findViewById(R.id.EditText01);
		String str = et.getText().toString();
		text.setText(text.getText().toString() + " Me : " + str + "\n");
		System.out.println("Sending message = " + str);

		for (MeshNode mn : root.getNeighbors().values()) {
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
