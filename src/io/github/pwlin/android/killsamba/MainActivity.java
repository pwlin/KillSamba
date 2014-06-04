package io.github.pwlin.android.killsamba;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.io.DataOutputStream;
import java.io.InputStream;

public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		try {
			// http://stackoverflow.com/questions/6625824/is-it-possible-to-force-stop-an-application-i-am-debugging-using-adb-in-termin
			this.runAsRoot(new String[] { "am force-stop com.funkyfresh.samba",
					"killall smbd", "killall nmbd",
					"am force-stop io.github.pwlin.android.killsamba" });
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

	// http://stackoverflow.com/questions/7449515/run-shell-commands-from-android-program
	public void runAsRoot(String[] cmds) throws Exception {
		Process p = Runtime.getRuntime().exec("su");
		DataOutputStream os = new DataOutputStream(p.getOutputStream());
		InputStream is = p.getInputStream();
		for (String tmpCmd : cmds) {
			os.writeBytes(tmpCmd + "\n");
			int readed = 0;
			byte[] buff = new byte[4096];
			// if cmd requires an output
			// due to the blocking behavior of read(...)
			boolean cmdRequiresAnOutput = false;
			if (cmdRequiresAnOutput) {
				while (is.available() > 0) {
					readed = is.read(buff);
					if (readed <= 0)
						break;
					String seg = new String(buff, 0, readed);
					Log.d("Kill Samba", "#> " + seg + " OK");
				}
			}
		}
		os.writeBytes("exit\n");
		os.flush();
		os.close();
	}

}
