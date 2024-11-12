package advancedProgProj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MetaTable 
{
	private final String race;
	private final String gender;
	private final String sampleID;   
	
	public String getRace() {
		return race;
	}

	public String getGender() {
		return gender;
	}

	public String getSampleID() {
		return sampleID;
	}

	public MetaTable(String s)
	{
		String[] splits = s.split("\t");
		this.sampleID = splits[0];
		this.race = splits[14];
		this.gender = splits[13];
	}
	
	//Static factory method
	public static HashMap<String, MetaTable> parseFile(String filePath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		HashMap<String, MetaTable> map = new HashMap<String, MetaTable>();
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			MetaTable m = new MetaTable(s);
			
			if (map.containsKey(m.getSampleID()))
				throw new Exception ("duplicatsampleid");
			
			map.put(m.getSampleID(), m);
		}
		reader.close();
		return map;
	}

}
