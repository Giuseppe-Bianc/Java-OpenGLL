package constant;

public class Const {

	private Const() {

	}

	public static final String PR = "assets/", OBJP = "Object ", PPNG = ".png";
	public static final String PRT = PR + "images/", BLN = PRT + "blendImage1" + PPNG;
	public static final String BLN2 = PRT + "blendImage2" + PPNG;
	public static final String QT = "'", ERR = "ERROR: ", ERR2 = ERR + QT;
	public static final String SPC = QT + "\n\t", LEND = "\r\n";
	public static final String SCF = "shader compilation failed.";
	public static final String TYP = "#type", VRT = "vertex", FRG = "fragment";
	public static final String RNG = "a-z", UNT = "Unexpected token " + QT;
	public static final String SHPT = PR + "shaders/default.glsl";
	public static final String PRF = PR + "fonts/Fira Code ";
	public static final String NRF = "Nerd Font";
	public static final String FNT = PRF + "Regular " + NRF + " Complete.ttf";
	public static final int UNN = 100, UND = UNN * 2, UNF = UND + 56, UNQ = UNN * 4, SD = 16;
	public static final boolean NRM = false;
}
