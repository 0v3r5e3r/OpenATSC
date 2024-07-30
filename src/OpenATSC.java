import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;

import openatsc.usbinterface.base.USBInterface;

public class OpenATSC {
	
	private static USBInterface usbInterface;
	
	public OpenATSC()
	{
		try {
			usbInterface = new USBInterface();
			usbInterface.scanDevicesForTuner();
		}catch(LibUsbException e) {
			e.printStackTrace();
			return;
		}
	}
	
	public static void main(String[] args)
	{
		new OpenATSC();
		if(USBInterface.getGlobalContext() != null)
			LibUsb.exit(USBInterface.getGlobalContext());
	}
	
}
