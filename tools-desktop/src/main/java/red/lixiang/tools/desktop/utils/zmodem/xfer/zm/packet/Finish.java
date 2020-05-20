package red.lixiang.tools.desktop.utils.zmodem.xfer.zm.packet;


import red.lixiang.tools.desktop.utils.zmodem.xfer.util.Buffer;
import red.lixiang.tools.desktop.utils.zmodem.xfer.util.ByteBuffer;
import red.lixiang.tools.desktop.utils.zmodem.xfer.zm.util.ZMPacket;

public class Finish extends ZMPacket {

	@Override
	public Buffer marshall() {
		ByteBuffer buff = ByteBuffer.allocateDirect(16);
		
		for(int i=0;i<2;i++)
			buff.put((byte) 'O');
		
		buff.flip();
		
		return buff;
	}
	
	@Override
	public String toString() {
		return "Finish: OO";
	}

}
