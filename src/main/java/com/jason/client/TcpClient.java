package com.jason.client;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
public class TcpClient {

	public static void main(String []args) throws Exception{
		Socket s = new Socket("127.0.0.1",8081);
		OutputStream os = s.getOutputStream();
		int request = 0;
		int testNum = 100;
		while(testNum > 0) {
			DataOutputStream dataOut = new DataOutputStream(os);
			String command = "user@login";
			byte[] commandBytes = new byte[32];
			byte[] beforeCommBytes = command.getBytes("UTF-8");
			for(int tag = 0; tag <beforeCommBytes.length; tag++) {
				commandBytes[tag] = beforeCommBytes[tag];
			}
			request++;
			
			Map<String, String> contentMap = new HashMap<String, String>();
			contentMap.put("username", "final");
			contentMap.put("password", "123");
			StringBuffer sb = new StringBuffer();
			for(Entry<String, String> entry : contentMap.entrySet()) {
				if(sb.length() > 0) {
					sb.append("&");
				}
				sb.append(entry.getKey()).append("=").append(entry.getValue());
			}
			byte[] content = sb.toString().getBytes("UTF-8");
			int length = 32 + 4 + content.length;
			dataOut.writeInt(length);
			dataOut.write(commandBytes);
			dataOut.writeInt(request);
			dataOut.write(content);
			os.flush();
			testNum--;
			Thread.currentThread();
			Thread.sleep(30*1000l);
		}
		os.close();
		s.close();
	}
}
