class Main {
    public static void main(String args[]){
    	Model m;
    	
    	m = Storage.grabModel("contactsModel.ser");
    	
    	if(m == null){
    		m = new Model();
    	}
    	
        new Controller(m, new View(m));
    }
}
