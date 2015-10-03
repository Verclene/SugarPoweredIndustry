package net.blacklab.spi.api;

public interface ISPObject {
	
	/**
	 * 保有するSPを取得する。
	 * @return
	 */
	int getSP();
	
	/**
	 * 保有するSPを設定する。
	 * @param value
	 */
	void setSP(int value);
	
	/**
	 * SP値を加える。負の値を指定して、SP値を減らすのに使うべきではない。
	 * @param value
	 */
	void addSP(int value);
	
	/**
	 * 保管可能な最大SPを取得する。
	 * @return
	 */
	int getMaxSP();
	
	/**
	 * ISPObjectを特定しSPを送信する。
	 * @param value
	 * @param spObject
	 */
	public void sendSPToObject(int value, ISPObject spObject);

	/**
	 * SPを受け取る際の処理。falseを返すと受け取らなくなる。
	 * @param value
	 * @param spObject
	 */
	public boolean onReceiveSP(int value, ISPObject spObject);

}
