package com.yzm.sleep;

public class OrangeRule {

	/**
	 * 
	 * @author 黄敏
	 * 
	 * 
	 * <h1>1. 整理代码</h1>
	 * <ul>
	 *    <li>1.1. Java 代码中不允许出现在警告，无法消除的警告要用 @SuppressWarnings。
	 *    <li>1.2. 去掉无用的包、方法、变量等，减少僵尸代码。
	 *    <li>1.3. 使用 Ctrl+Shift+F 来格式化代码，然后再进行调整。
	 *    <li>1.4. 使用 Ctrl+Shift+O 来格式化 Import 包。
	 * </ul>
	 * 
	 * <h2>2. 命名规则</h2>
	 *    <h2>2.1. 基本原则</h2>
	 *    <ul>
	 *         <li>2.1.1. 变量，方法，类命名要表义，严格禁止使用 name1, name2 等命名。
	 *         <li>2.1.2. 命名不能太长，适当使用简写或缩写。（最好不要超过 25 个字母）
	 *         <li>2.1.3. 方法名以小写字母开始，以后每个单词首字母大写。
	 *         <li>2.1.4. 避免使用相似或者仅在大小写上有区别的名字。
	 *     </ul>
	 *    
	 *    <h2>2.2. 类、接口</h2>
	 *    <ul>
	 *         <li>2.2.1. 所有单词首字母都大写。使用能确切反应该类、接口含义、功能等的词。一般采用名词。
	 *         <li>2.2.2. 接口带 I 前缀，或able, ible, er等后缀。如ISeriable。
	 *    </ul>
	 *    
	 *    <h2>2.3. 字段、常量</h2>
	 *    <ul>
	 *         <li>2.3.1. 成员变量以 m 开头，静态变量以 s 开头，如 mUserName, sInstance。
	 *         <li>2.3.2. 常量全部大写，在词与词之前用下划线连接，如 MAX_NUMBER。
	 *         <li>2.3.3. 代码中禁止使用硬编码，把一些数字或字符串定义成常用量。
	 *         <li>2.3.4. 对于废弃不用的函数，为了保持兼容性，通常添加 @Deprecated，如 {@link #doSomething()}
	 *    </ul>* 
	 * 
	 * <h3>1. 整理代码</h3>
	 * <ul>
	 *    <li>1.1. 一切按照整体框架来，比如网络请求框架，数据库存储框架，禁止自由发挥，如现有框架不满足需求要引进其他写法，请与组长交涉
	 *    <li>1.2. 禁止成员私自更改共有结构，比如，数据库结构、页面排列组合结构
	 *    <li>1.3. 自己做的内容与其他组员做的内容有冲突或者需要更改的，请与当事人协商。不得私自更改
	 *    <li>1.4. 遇到比较复杂的逻辑和思想需要再做之前提前与组长沟通。
	 * </ul>
	 * 
	 */
	
	/**
	 * custom drawable
	 *   custom_circle_point_7e7c91  自定义圆  颜色值 #7e7c91 无边框
	 *   custom_circle_point_theme  自定义圆  颜色值:主题色, 无边框
	 *   custom_seekbar_thumb_cricle 自定义圆 颜色值 #e0e0f6 size 22dp 边框 6dp 颜色 #8745ff
	 *   custom_circle_point_e0e0f6  自定义圆    白色   颜色值 #e0e0f6 无边框
	 *   dialog_closed_icon 定义dialog 取消  图片
	 *   dialog_finish_icon 定义dialog 完成  图片
	 *   custom_button_bg 自动义 按钮背景 颜色值 #8745ff 圆角 4dp 边框 0.5dp 颜色 #525266  点击 效果 颜色透明 50%
	 *   custom_broder_525266 自定义边框  无背景  圆角4dp 边框 0.5dp 延迟#525266  无点击效果
	 *   tranceparent_button_bg 自定义透明按钮 圆角4dp 灰色边框  边框 0.5dp 点击后背景 #7F30304c
	 *   button_bg_525266 自定义灰色按钮 色值#ff525266  圆角4dp 灰色边框  边框 0.5dp  点击后透明背景 有边框 #ff525266
	 *   comm_bg_listview_item 自定义 listView Item 背景 颜色 #ff30304c 点击效果 颜色 #7f30304c
	 *   
	 *   circular_corner_dialog3 自定义矩形背景  颜色值 #3c3c5e 圆角 4dp 
	 *   
	 *   StringUtil 工具类，字符串验证正则表达式集合
	 *   round_btn_bg 灰边白底的圆形自定义图片
	 *   R.drawable.custom_round_normal_small 矩形圆角 4dp 颜色：内容背景色
	 *   R.drawable.custom_round_select_small 矩形圆角 4dp 颜色：主题色
	 *   
	 */
	
}
