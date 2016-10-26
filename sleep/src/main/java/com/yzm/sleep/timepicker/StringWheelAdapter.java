/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.yzm.sleep.timepicker;

import java.util.ArrayList;
import java.util.List;


/**
 * Numeric Wheel adapter.
 */
public class StringWheelAdapter implements WheelAdapter {
	
//	/** The default min value */
//	public static final int DEFAULT_MAX_VALUE = 9;
//
//	/** The default max value */
//	private static final int DEFAULT_MIN_VALUE = 0;
	
	// Values
//	private int minValue;
//	private int maxValue;
	private List<String> defaultListValue = new ArrayList<String>();
	private List<String> listValue;
	
	// format
	private String format;
	
	/**
	 * Default constructor
	 */
//	public StringWheelAdapter() {
//		this(DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
//	}

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 */
	public StringWheelAdapter(List<String> arrayListValue) {
		this(arrayListValue, null);
	}

	/**
	 * Constructor
	 * @param minValue the wheel min value
	 * @param maxValue the wheel max value
	 * @param format the format string
	 */
	public StringWheelAdapter(List<String> arrayListValue, String format) {
		this.listValue = arrayListValue;
		this.format = format;
	}

	@Override
	public String getItem(int index) {
		if (index >= 0 && index < getItemsCount()) {
			String value = listValue.get(index);
			return format != null ? String.format(format, value) : value;
		}
		return null;
	}

	@Override
	public int getItemsCount() {
		return listValue.size();
	}
	
	@Override
	public int getMaximumLength() {
		int max = Math.max(Math.abs(listValue.get(0).length()), Math.abs(listValue.get(listValue.size()-1).length()));
		return max;
	}
}
