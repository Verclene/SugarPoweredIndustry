package net.blacklab.spi.api;

public interface ISPObject {
	
	/**
	 * 保有するSPを取得する。
	 * @return
	 */
	float getSP();
	
	/**
	 * 保有するSPを設定する。
	 * @param value
	 */
	void setSP(float value);
	
	/**
	 * SP値を加える。返り値は、SP値が追加されたかを返す。
	 * @param value
	 */
	boolean addSP(float value);
	
	/**
	 * 保管可能な最大SPを取得する。
	 * @return
	 */
	float getMaxSP();
	
	/**
	 * ISPObjectを特定しSPを送信する。相手先にSPが正常に追加された場合、trueを返すこと。
	 * @param value
	 * @param spObject 送信「先」ターゲット
	 */
	public boolean sendSPToObject(float value, ISPObject spObject);
	
	/**
	 * 1更新毎に、SPを受け取る量を返す。
	 * @param value
	 * @param spObject 送信元ターゲット
	 * @return
	 */
	public float amountReceiveSPperUpdate(ISPObject spObject);
	
	/**
	 * SPを受け取る際の処理。falseを返すと受け取らなくなる。
	 * @param value
	 * @param spObject 送信元ターゲット
	 */
	public boolean onReceiveSP(float value, ISPObject spObject);
	
}
