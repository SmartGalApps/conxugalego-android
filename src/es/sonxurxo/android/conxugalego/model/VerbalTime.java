package es.sonxurxo.android.conxugalego.model;

import java.io.Serializable;

public class VerbalTime implements Serializable, Comparable<VerbalTime> {

	private static final long serialVersionUID = -833922642023923257L;
	public static final String CHEAT_MARK = "#";

	private String name;
	private int order;
	private String[] times;

	/**
	 * @param name
	 * @param times
	 */
	public VerbalTime(String name, String[] times) {
		super();
		this.name = name;
		this.times = times;
		if (name.equals("PI")) {
			this.order = 0;
		} else if (name.equals("EI")) {
			this.order = 1;
		} else if (name.equals("II")) {
			this.order = 2;
		} else if (name.equals("MI")) {
			this.order = 3;
		} else if (name.equals("FI")) {
			this.order = 4;
		} else if (name.equals("TI")) {
			this.order = 5;
		} else if (name.equals("PS")) {
			this.order = 6;
		} else if (name.equals("IS")) {
			this.order = 7;
		} else if (name.equals("FS")) {
			this.order = 8;
		} else if (name.equals("IP")) {
			this.order = 9;
		} else if (name.equals("IA")) {
			this.order = 10;
		} else if (name.equals("IN")) {
			this.order = 11;
		} else if (name.equals("FN")) {
			this.order = 12;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getTimes() {
		return this.times;
	}

	public void setTimes(String[] times) {
		this.times = times;
	}

	@Override
	public String toString() {
		String result = this.name + ": ";
		for (String time : this.times) {
			result += time + ",";
		}
		return result.substring(0, result.length() - 1);
	}

	@Override
	public int compareTo(VerbalTime other) {
		return new Integer(this.order).compareTo(new Integer(other.order));
	}

}
