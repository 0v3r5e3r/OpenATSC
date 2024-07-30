import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import openatsc.usbinterface.Interface;

public class OpenATSC {
	
	private static Interface usbInterface;
	
	public OpenATSC()
	{
		try {
			usbInterface = new Interface();
			usbInterface.scanDevicesForTuner();
		}catch(LibUsbException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void main(String[] args)
	{
		new OpenATSC();
		if(Interface.getGlobalContext() != null)
			LibUsb.exit(Interface.getGlobalContext());
	}
	
}
