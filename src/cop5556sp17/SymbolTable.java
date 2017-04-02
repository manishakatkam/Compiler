package cop5556sp17;



import java.util.ArrayList;
import java.util.HashMap;

import cop5556sp17.AST.Dec;


public class SymbolTable {
	//TODO  add fields
	class values{
		public
			int valuescope;
			Dec declaration;
			public values() {
				valuescope = 0;
				declaration = null;
			}
			
			public values(int n, Dec d){
				valuescope = n;
				declaration = d;
			}
			
	}
	ArrayList<Integer> stackscope;
	int next_scope, current_scope;
	HashMap<String, ArrayList<values>> hashtable;
	
	/** 
	 * to be called when block entered
	 */
	public void enterScope(){
		//TODO:  IMPLEMENT THIS
		current_scope = next_scope++;
		stackscope.add(new Integer(current_scope));
	}
	
	
	
	/**
	 * leaves scope
	 */
	public void leaveScope(){
		//TODO:  IMPLEMENT THIS
		int stop=stackscope.size()-1;
		stackscope.remove(stop);
		stop=stop-1;
		current_scope = stackscope.get(stop);
	}
	
	public boolean insert(String ident, Dec dec){
		//TODO:  IMPLEMENT THIS
		ArrayList<values> a = hashtable.get(ident);
		if(a!=null){
			for(int i=0;i<a.size();i++){
				if(a.get(i).valuescope==current_scope)
					return false;
			}
		}
		if (a ==null){
			a = new ArrayList<values>();
			hashtable.put(ident, a);
		}
		a.add(new values(current_scope, dec));
		return true;
	}
	
	public Dec lookup(String ident){
		//TODO:  IMPLEMENT THIS
		ArrayList<values> a = hashtable.get(ident);
		if(a==null)
			return null;
		int i=stackscope.size()-1;
		for(int j=i; j>=0; j--){
			       for(int k=0; k<a.size(); k++){
				          if (a.get(k).valuescope==stackscope.get(j)){
				        	  return a.get(k).declaration;
				          }
                                   					
			        }
		}
		
		return null;
		
	}
		
	public SymbolTable() {
		//TODO:  IMPLEMENT THIS
		hashtable = new HashMap<>();
	    stackscope = new ArrayList<Integer>();
		current_scope = 0;
		stackscope.add(current_scope);
		next_scope = 1;
	}


	@Override
	public String toString() {
		//TODO:  IMPLEMENT THIS
		return "";
	}
	
	


}
