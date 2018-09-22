package util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ErrorLogST {
	
	private static ErrorLogST el;
	
	private ErrorLogST () {
	}
	
	public void clearLog () 
			throws IOException {
		File f = new File("C:/temp/PaulistaLog.txt");
		f.delete();
	}
	
	public void addError (Exception e) 
			throws IOException {
		File f = new File("C:/temp/PaulistaLog.txt");
		
		String space = "\n==================== \n\n";
		 StringBuilder sb = new StringBuilder(space)
				.append("Tipo: ").append(e.getClass().getName())
				.append("\nMensagem: ").append(e.getMessage());
		
		for(StackTraceElement ste : e.getStackTrace()) {
			sb.append(ste.toString()).append("\n");
		}
		String text = sb.toString();
		System.out.println(text);
		
		FileWriter fw = new FileWriter(f);
		fw.write(text);
		fw.flush();
		fw.close();
	}
	
	public static ErrorLogST getErrorLog () {
		if (el == null) {
			instantiate ();
		}
		return el;
	}
	
	private static void instantiate () {
		el = new ErrorLogST();
	}

}
