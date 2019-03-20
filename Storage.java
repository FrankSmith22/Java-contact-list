import java.io.*;

class Storage implements  Serializable{
	
	public static Model grabModel(String path){
		return (Model)unserialize("contactsModel.ser");
	}
	
	
	public static void serialize(String s, Object o){
		try{
			FileOutputStream fos = new FileOutputStream( s );
			ObjectOutputStream oos = new ObjectOutputStream( fos );
			oos.writeObject( o );
			oos.close();
			fos.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	}
	
	public static Object unserialize(String s){
		Object o = null;
		try{
			FileInputStream fis = new FileInputStream(s);
			ObjectInputStream ois = new ObjectInputStream(fis);
			o = ois.readObject();
			ois.close();
			fis.close();
		}
		catch(Exception e){
			System.out.println(e.toString());
		}
	return o;
	}
}
