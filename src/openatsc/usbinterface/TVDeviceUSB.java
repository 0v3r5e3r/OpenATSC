package openatsc.usbinterface;

import org.usb4java.ConfigDescriptor;
import org.usb4java.Device;
import org.usb4java.DeviceHandle;
import org.usb4java.LibUsb;

public class TVDeviceUSB extends TVDevice {
	
	protected static final String TAG = "TVDeviceUSB";
	protected DeviceHandle connection;
	protected Device device;
	protected int[] mEndpointBufferSizes;
	protected byte[][] mEndpointBuffers;
	protected Object[] mEndpoints;
	protected Object mInterface;
	
	public TVDeviceUSB(int deviceType, String deviceName, Device device) {
		super(deviceType, 2, deviceName);
		this.device = device;
		this.mEndpointBuffers = new byte[256][];
		this.mEndpointBufferSizes = new int[256];
		this.connection = new DeviceHandle();
	}

	
	protected void createUsbConnection() {
		int result = LibUsb.open(this.device, this.connection);
		if(result == LibUsb.SUCCESS) {
			System.out.println("Managed to open the USB Connection!");
			int interfaceResult = LibUsb.claimInterface(this.connection, 0);
			if(interfaceResult == LibUsb.SUCCESS)
			{
				//TODO: run the program, you have the endpoint addresses in the configs somehwere
				// You need to extract the endpoint addresses and save them,
				// You also need to extract the interface and save it.
				// Then you can resume working on the full TVDeviceUSB which in the padtv app is called "EyeTVDeviceUSB"
				
				try {
					ConfigDescriptor configs = new ConfigDescriptor();
					LibUsb.getActiveConfigDescriptor(device, configs);
					System.out.println(configs);
				}finally {
					LibUsb.releaseInterface(connection, 0);
				}
			}
		}else {
			System.out.println("Couldn't open the device!");
		}
	}
}
