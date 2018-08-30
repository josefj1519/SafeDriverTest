
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;


//Josef Jankowski 3/06/18

public class FileDriverV2 implements Serializable {
	
	//serialization is platform independent
	private static final long serialVersionUID = 1L;

	ObjectInputStream in;
	ObjectOutputStream out;
	
	
	FileOutputStream fileout;
	FileInputStream filein;
	

	File file;
	FileWriter write;
	FileReader read;
	BufferedReader buffer;
	
	String filepath;
	String mainpath;
	
	ArrayList<File> folderloc = new ArrayList<File>();
	
	public FileDriverV2() {	
		
		mainpath = System.getProperty("user.home")+"/Documents/";
		
		mainpath = mainpath.replace("\\", "/");
	
		filepath = "";
		
	}
	
	private String getFolder(String parentfolder) {
		
		if(parentfolder == null) {
			return "";
		}
		
		for(int i = 0; i<folderloc.size();i++) {
			if(folderloc.get(i).getName().equals(parentfolder)) {
				filepath = folderloc.get(i).getAbsolutePath();
				filepath = filepath.replace("\\", "/");
				//System.out.println("parent path: "+ filepath);
			}
		}
		System.out.println(filepath+"/");
		return filepath+"/";
	}
	
	public void createFolder(String folder, String parentfolder) {
		
		filepath = this.getFolder(parentfolder);
		
		filepath = filepath.replace(mainpath, "");
		
		//System.out.println("filepath:" + filepath);
		file = new File(mainpath + "/"+filepath, folder);
		
		//System.out.println("folder: "+ file);
		
		if(!file.exists()) {
			file.mkdir();
		}
		
		else {
		System.out.println("folder exists");
		}
		folderloc.add(file);		
	}
	
	
	public void saveObj(Object obj, String filename, String folder) {
		
	filepath = this.getFolder(folder);
	
		
		try {
		//System.out.println("save obj:" + filepath+filename+".ser");
			fileout = new FileOutputStream(filepath + filename+".ser");
			out = new ObjectOutputStream(fileout);
			out.writeObject(obj);
			out.close();
			fileout.close();
		}catch(IOException i) {
			i.printStackTrace();
			
		}
		
		
	}
	
	
	public Object readObj(String filename, String folder){
		Object obj;
		
		filepath = this.getFolder(folder);
		
		try {
		//	System.out.println("read obj:" + filepath + filename+".ser");
			filein = new FileInputStream(filepath+filename+".ser"); 
			in = new ObjectInputStream(filein);			
			obj = in.readObject();
			in.close();
			filein.close();
			return obj;
		}catch(ClassNotFoundException c) {
			c.printStackTrace();
		}catch(IOException i) {
			i.printStackTrace();
		}
		
		return null;
		
	}
	
	
	public void saveText(String txt, String filename, String folder) {
		
		filepath = this.getFolder(folder);
		
		try {
		System.out.println("save txt:" + filepath+filename+".txt");
			file = new File(filepath+filename+".txt");
			write = new FileWriter(file);
			write.write(txt);
			write.flush();
			write.close();
		}catch(IOException i) {
			i.printStackTrace();
		}
	}
	
	
	public String readText(String filename, String folder, boolean resource) {
		
		String txt = "";
		
		try {
			if(resource) {
				filepath = this.getClass().getResource(filename).getPath();
				filename = "";
			}
			else if(folder != null){
				filepath = this.getFolder(folder);
			}
		//System.out.println("read txt:" + filepath+filename+".txt");
			
			file = new File(filepath+filename+ ".txt");
			read = new FileReader(file);
			buffer = new BufferedReader(read);
			txt = buffer.readLine();
			buffer.close();
			return txt;
			
		}catch(FileNotFoundException ex) {
			ex.printStackTrace();
		}catch(IOException i) {
			i.printStackTrace();
		}
		
		return "";
	}
	
	public void deleteText(String filename, String folder) {
		filepath = this.getFolder(folder);
		file = new File(filepath, filename+".txt");
		file.delete();
		
	}
	
	public void deleteObj(String filename, String folder) {
		filepath = this.getFolder(folder);
		file = new File(filepath, filename+".ser");
		file.delete();
	}

}
