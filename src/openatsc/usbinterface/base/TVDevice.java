package openatsc.usbinterface.base;

import openatsc.usbinterface.GenericDevice;

public abstract class TVDevice {

	
	public static class ConnectionType {
		public static final int NETWORK = 1;
		public static final int SPI = 4;
		public static final int STATIC = 3;
		public static final int UNKNOWN = 0;
		public static final int USB = 2;
	}
	
	public static class DeviceType {
		public static final int EYETV = 1;
		public static final int FAKE = 8;
		public static final int FILESTREAM = 4;
		public static final int MERON = 6;
		public static final int NETSTREAM = 3;
		public static final int SATIP = 5;
		public static final int SIANOSPI = 9;
		public static final int TIVIZEN = 2;
		public static final int UNKNOWN = 0;
		public static final int USB = 7;
	}
	
	public interface GenericDeviceListener {
		void CEDeviceCommandFinished(TVDevice device, int i, long j, int i2);
		void CEDeviceCommandProgress(TVDevice device, int i, long j, double d);
		void CEDeviceDataArrived(TVDevice device);
		void CEDeviceDidInitialize(TVDevice device, int i, long j);
		void CEDeviceDidStartReceiving(TVDevice device, int i, long j);
		void CEDeviceDidTune(TVDevice device, int i, long j, boolean z);
		//void CEDeviceGotProperty(TVDevice device, int i, long j, GenericDeviceProperty genericDeviceProperty);
		void CEDeviceInitializationStateUpdate(TVDevice device, int i, int i2, int i3, int i4);
		void CEDeviceJSONCommandExecuted(TVDevice device, int i, long j, String str);
		//void CEDevicePropertyChanged(TVDevice device, int i, GenericDeviceProperty genericDeviceProperty, GenericDeviceProperty genericDeviceProperty2);
	}

	public interface PlayerControllerListener{
		
	}
	
	protected int m_devicetype, m_connectiontype, m_previousInitializationState;
	protected String m_devicename;
	protected Object m_genericDeviceLock = new Object();
	protected float m_deviceInitializationProgress = 0.0f, m_deviceInitializationProgressMax = 1.0f;
	protected float m_initializationProgress = 0.0f, m_initializationProgressGenericDevice = 20.0f, m_initializationProgressChannelList = 70.0f, m_initializationProgressEnd = 10.0f;
	protected final int m_initializationProgressTotal = 100;
	protected float m_previousInitalizationState = 0.0f;
	protected boolean m_locked = false, m_tuneCommandFinished = false;
	
	protected GenericDevice m_genericDevice = null;
	
	public TVDevice(int deviceType, int connectionType, String deviceName)
	{
		m_devicetype = deviceType;
		m_connectiontype = connectionType;
		m_devicename = deviceName;
		if(deviceName == null)
			m_devicename = "";
	}
	
	public void createGenericDevice() {
		synchronized(m_genericDeviceLock) {
			m_initializationProgress = 0.0f;
			m_deviceInitializationProgress = 0.0f;
			m_previousInitalizationState = 0.0f;
			//this.m_genericDevice = new GenericDevice()
		}
	}
	
	public boolean equals(int deviceType, String deviceName, boolean doLog) {
		return (this.m_devicetype == deviceType && this.m_devicename.equals(deviceName));
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof TVDevice))
				return false;
		TVDevice device = (TVDevice)o;
		return equals(device.getDeviceType(), device.getDeviceName(), false);
	}
	
	public int getDeviceType() {
		return this.m_devicetype;
	}
	
	public void setDeviceName(String deviceName) {
		this.m_devicename = deviceName;
	}
	
	public String getDeviceName() {
		return this.m_devicename;
	}
}
