package advancedProgProj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

public class MetaTable 
{
	//binary things
	private final String race;
	private final String gender;
	private final String sampleID;
	private final String vitalStatus;
	private final String ethnicity;
	
	//non-binary things
//	private final int ageAtDiagnosis;
//	private final String sampleType;
//	private final String primary_site;
	
	public String getRace() {
		return race;
	}

	public String getGender() {
		return gender;
	}

	public String getSampleID() {
		return sampleID;
	}
	
	public String getEthnicity() {
		return ethnicity;
	}
	
	public String getVitalStatus() {
		return vitalStatus;
	}

	public String toString() {
		return "SampleID:\t" + getSampleID() + "\nVital Status:\t" + getVitalStatus() +
				"\nGender:\t\t" + getGender() + "\nRace:\t\t" + getRace() + "\nEthnicity:\t" + getEthnicity() + "\n";
		
	}

	public MetaTable(String s)
	{
		String[] splits = s.split(",");

		
		// Check if the required fields are valid
	    if (splits[0].isEmpty() || splits[14].isEmpty() || splits[13].isEmpty() ||
	        splits[12].isEmpty() || splits[15].isEmpty() ||
	        splits[14].equalsIgnoreCase("\"Not available\"") || splits[13].equalsIgnoreCase("\"Not available\"") ||
	        splits[12].equalsIgnoreCase("\"Not available\"") || splits[15].equalsIgnoreCase("\"Not available\"")) 
	    {
	        // If any of the required fields are missing or marked as "Not available", skip this line.
	        throw new IllegalArgumentException("Invalid data in line: " + splits[0].substring(1, splits[0].length()-1));
	    }
	        
		this.sampleID = splits[0].substring(1, splits[0].length()-1);
		this.vitalStatus = splits[12].substring(1, splits[12].length()-1);
		this.gender = splits[13].substring(1, splits[13].length()-1);
		this.race = splits[14].substring(1, splits[14].length()-1);		
		this.ethnicity = splits[15].substring(1, splits[15].length()-1);
	}
	
	//Static factory method to build objects with correct information
	public static HashMap<String, MetaTable> parseFile(String filePath) throws Exception
	{
		BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
		HashMap<String, MetaTable> map = new HashMap<String, MetaTable>();
		reader.readLine();
		for(String s = reader.readLine(); s != null; s = reader.readLine())
		{
			try
			{
				MetaTable m = new MetaTable(s);
				if (map.containsKey(m.getSampleID()))
					throw new Exception ("duplicate sampleid");
				map.put(m.getSampleID(), m);
			} 
			catch (IllegalArgumentException e) 
			{
				System.out.println("Skipping invalid data: " + e.getMessage());
			}
		}
		reader.close();
		return map;
	}
	
	
	
	public static void main(String[] args) throws Exception
	{
		String dataTablePath = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\TableS9_metadata_KrakenUniq_BIO_Fullset.csv";
		String readsTablePath = "C:\\Users\\Bryce\\Documents\\Classes\\Advanced Programming\\TableS8_T2T_KrakenUniq_BIO_Fullset.csv";
		HashMap<String, MetaTable> patientData = parseFile(dataTablePath);
		
		patientData.forEach((id,patient) -> {
			System.out.println(patient);
		});
	}

}
