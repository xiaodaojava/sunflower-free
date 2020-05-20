package red.lixiang.tools.desktop.utils.zmodem;


import red.lixiang.tools.desktop.utils.zmodem.util.FileAdapter;
import red.lixiang.tools.desktop.utils.zmodem.xfer.zm.util.ZModemReceive;
import red.lixiang.tools.desktop.utils.zmodem.xfer.zm.util.ZModemSend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;


public class ZModem {
	
	private InputStream netIs;
	private OutputStream netOs;

	// 这里应该是用shell的outputStream和 InputStream
	public ZModem(InputStream netin,OutputStream netout){
		netIs  = netin;
		netOs  = netout;
		
	}

    /**
     * 接收文件
	 * @param destDir
     * @throws IOException
	 */
	public void receive(FileAdapter destDir) throws IOException{
		ZModemReceive receiver = new ZModemReceive(destDir, netIs, netOs);
		receiver.receive();
		netOs.flush();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
	
	public void send(Map<String,FileAdapter> lst) throws IOException{
		ZModemSend sender = new ZModemSend(lst, netIs, netOs);
		sender.send();
		netOs.flush();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}
