package com.yzm.sleep.utils;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.text.TextUtils;

public class ContactsUtils {
	// 获取库phone表字段
	private static final String[] PHONES_PROJECTION = new String[] {
			Phone.DISPLAY_NAME, Phone.NUMBER, Photo.PHOTO_ID, Phone.CONTACT_ID };
	// 电话号码
	private static final int PHONES_NUMBER_INDEX = 1;
	private static ArrayList<String> contactsPhoneNumber = new ArrayList<String>();

	public static ArrayList<String> getContactsPhoneNumber(Context context) {
		contactsPhoneNumber.clear();
		ContentResolver mContentResolver = context.getContentResolver();
		// 获取手机联系人
		Cursor phoneCursor = mContentResolver.query(Phone.CONTENT_URI,
				PHONES_PROJECTION, null, null, null);
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {

				// 得到手机号码
				String phoneNum = phoneCursor.getString(PHONES_NUMBER_INDEX);
				if (phoneNum.substring(0, 1).equals("+")) {
					phoneNum = phoneNum.substring(3);
				}
				phoneNum = phoneNum.replace(" ", "");
				// 当手机号码为空或者为空字段跳过当前循环
				if (TextUtils.isEmpty(phoneNum))
					continue;
				if (StringUtil.isMobile(phoneNum)) {
					contactsPhoneNumber.add(phoneNum);
				}
			}
			phoneCursor.close();
		}
		return contactsPhoneNumber;
	}
}
