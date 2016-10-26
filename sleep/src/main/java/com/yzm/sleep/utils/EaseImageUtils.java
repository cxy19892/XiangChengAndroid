/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yzm.sleep.utils;

import com.easemob.util.PathUtil;

public class EaseImageUtils extends com.easemob.util.ImageUtils {
	public static String getImagePath(String remoteUrl) {
		String imageName = remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1,
				remoteUrl.length());
		String path = PathUtil.getInstance().getImagePath() + "/" + imageName;
		return path;

	}

	public static String getThumbnailImagePath(String thumbRemoteUrl) {
		String thumbImageName = thumbRemoteUrl.substring(
				thumbRemoteUrl.lastIndexOf("/") + 1, thumbRemoteUrl.length());
		String path = PathUtil.getInstance().getImagePath() + "/" + "th"
				+ thumbImageName;
		return path;
	}

}
