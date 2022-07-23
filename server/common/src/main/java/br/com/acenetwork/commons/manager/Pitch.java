package br.com.acenetwork.commons.manager;

public class Pitch
{
	public static final float Gb0 = 0.500F;
	public static final float G0 = 0.530F;
	public static final float Ab0 = 0.561F;
	public static final float A0 = 0.595F;
	public static final float Bb0 = 0.630F;
	public static final float B0 = 0.667F;
	public static final float C1 = 0.707F;
	public static final float Db1 = 0.749F;
	public static final float D1 = 0.794F;
	public static final float Eb1 = 0.841F;
	public static final float E1 = 0.891F;
	public static final float F1 = 0.944F;
	public static final float Gb1 = 1.000F;
	public static final float G1 = 1.059F;
	public static final float Ab1 = 1.122F;
	public static final float A1 = 1.189F;
	public static final float Bb1 = 1.260F;
	public static final float B1 = 1.335F;
	public static final float C2 = 1.414F;
	public static final float Db2 = 1.498F;
	public static final float D2 = 1.587F;
	public static final float Eb2 = 1.682F;
	public static final float E2 = 1.782F;
	public static final float F2 = 1.888F;
	public static final float Gb2 = 2.000F;
	
	public static float getPitch(int note)
	{
		if(note < 0)
		{
			return Gb0;
		}
		
		switch(note)
		{
		case 0:
			return Gb0;
		case 1:
			return G0;
		case 2:
			return Ab0;
		case 3:
			return A0;
		case 4:
			return Bb0;
		case 5:
			return B0;
		case 6:
			return C1;
		case 7:
			return Db1;
		case 8:
			return D1;
		case 9:
			return Eb1;
		case 10:
			return E1;
		case 11:
			return F1;
		case 12:
			return Gb1;
		case 13:
			return G1;
		case 14:
			return Ab1;
		case 15:
			return A1;
		case 16:
			return Bb1;
		case 17:
			return B1;
		case 18:
			return C2;
		case 19:
			return Db2;
		case 20:
			return D2;
		case 21:
			return Eb2;
		case 22:
			return E2;
		case 23:
			return F2;
		default:
			return Gb2;
		}
	}
	
	public static float getLowestNote()
	{
		return Gb0;
	}
	
	public static float getHighestNote()
	{
		return Gb2;
	}
}