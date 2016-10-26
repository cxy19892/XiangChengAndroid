package com.yzm.sleep.bluetoothNRF;

/**
 * DFUServiceParser is responsible to parse sccanning data and it check if scanned device has DFU service in it
 */
public class DFUServiceParser {
	private final String TAG = "DFUServiceParser";
	private final int SERVICE_CLASS_128BIT_UUID = 6;
	private final String DFU_SERVICE_UUID = "2148"; // 0x1530	
	private int packetLength = 0;
	private boolean isValidDFUSensor = false;

	private static DFUServiceParser mParserInstance;

	/**
	 * singleton implementation of DFUServiceParser
	 */
	public static synchronized DFUServiceParser getDFUParser() {
		if (mParserInstance == null) {
			mParserInstance = new DFUServiceParser();
		}
		return mParserInstance;
	}

	public boolean isValidDFUSensor() {
		return isValidDFUSensor;
	}

	/**
	 * It will check field name Service Class UUID with value for 128-bit-Service-UUID = {6}
	 */
	public void decodeDFUAdvData(byte[] data) throws Exception {
		isValidDFUSensor = false;
		if (data != null) {
			int fieldLength, fieldName;
			packetLength = data.length;
			for (int index = 0; index < packetLength; index++) {
				fieldLength = data[index];
				if (fieldLength == 0) {
					DFUManager.log(TAG, "index: " + index + " No more data exist in Advertisement packet");
					return;
				}
				fieldName = data[++index];
				DFUManager.log(TAG, "fieldName: " + fieldName + " Filed Length: " + fieldLength);

				if (fieldName == SERVICE_CLASS_128BIT_UUID) {
					DFUManager.log(TAG, "index: " + index + " Service class 128 bit UUID exist");
					decodeService128BitUUID(data, index + 1, fieldLength - 1);
					index += fieldLength - 1;
				} else {
					// Other Field Name						
					index += fieldLength - 1;
				}
			}
		} else {
			DFUManager.log(TAG, "data is null!");
			return;
		}
	}

	/**
	 * check for required DFU Service UUID = 0x1530 or in decimal 2148 inside 128 bit uuid
	 */
	private void decodeService128BitUUID(byte[] data, int startPosition, int serviceDataLength) throws Exception {
		DFUManager.log(TAG, "StartPosition: " + startPosition + " Data length: " + serviceDataLength);
		String ServiceUUID = Byte.toString(data[startPosition + serviceDataLength - 3]) + Byte.toString(data[startPosition + serviceDataLength - 4]);
		if (ServiceUUID.equals(DFU_SERVICE_UUID)) {
			DFUManager.log(TAG, "Service UUID: " + ServiceUUID);
			DFUManager.log(TAG, "DFU service exist!");
			isValidDFUSensor = true;
		}
	}

}
